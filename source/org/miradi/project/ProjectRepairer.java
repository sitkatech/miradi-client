/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import java.awt.Dimension;
import java.awt.Point;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.PoolWithIdAssigner;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectRepairer
{
	public static void scanForCorruptedObjects(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.possiblyShowMissingObjectsWarningDialog();
	}
	
	public static void repairAnyProblems(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.repair();
	}
	
	public ProjectRepairer(Project projectToRepair)
	{
		project = projectToRepair;
	}
	
	void repair() throws Exception
	{
		repairUnsnappedNodes();
		deleteOrphanAnnotations();
		repairDiagramObjectsReferringToNonExistantTags();
	}

	 
	
	private void repairUnsnappedNodes()
	{
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		for (int i = 0; i < diagramFactors.length; ++i)
		{
			fixLocation(diagramFactors[i]);
			fixSize(diagramFactors[i]);
		}
	}

	private void fixSize(DiagramFactor diagramFactor)
	{
		Dimension currentSize = diagramFactor.getSize();
		int snappedEvenWidth = project.forceNonZeroEvenSnap(currentSize.width);
		int snappedEvenHeight = project.forceNonZeroEvenSnap(currentSize.height);
		Dimension snappedEvenSize = new Dimension(snappedEvenWidth, snappedEvenHeight);
		
		if (currentSize.equals(snappedEvenSize))
			return;
		
		try
		{
			String newSizeAsString = EnhancedJsonObject.convertFromDimension(snappedEvenSize);
			project.setObjectData(diagramFactor.getType(), diagramFactor.getId(), DiagramFactor.TAG_SIZE, newSizeAsString);
		}
		catch(Exception e)
		{
			logAndContinue(e);
		}
	}

	private void fixLocation(DiagramFactor diagramFactor)
	{
		Point currentLocation = diagramFactor.getLocation();
		Point expectedLocation  = project.getSnapped(currentLocation);
		int deltaX = expectedLocation.x - currentLocation.x;
		int deltaY = expectedLocation.y - currentLocation.y;

		if(deltaX == 0 && deltaY == 0)
			return;
			
		try
		{
			String moveToLocation = EnhancedJsonObject.convertFromPoint(expectedLocation);
			project.setObjectData(diagramFactor.getType(), diagramFactor.getId(), DiagramFactor.TAG_LOCATION, moveToLocation);
		}
		catch(Exception e)
		{
			logAndContinue(e);
		}
	}
	
	public void deleteOrphanAnnotations()
	{
		deleteOrphanAnnotations(ObjectType.OBJECTIVE);
		deleteOrphanAnnotations(ObjectType.GOAL);
		deleteOrphanAnnotations(ObjectType.INDICATOR);
	}

	private void deleteOrphanAnnotations(int annotationType)
	{
		IdList allIds = project.getPool(annotationType).getIdList();
		for(int i = 0; i < allIds.size(); ++i)
		{
			BaseId annotationId = allIds.get(i);
			try
			{
				BaseObject object = project.getObjectManager().findObject(annotationType, annotationId);
				BaseObject owner = object.getOwner();
				if(owner == null)
				{
					EAM.logWarning("Found orphan " + annotationType + ":" + annotationId);
				}
			}
			catch(Exception e)
			{
				logAndContinue(e);
			}
		}
	}

	private void logAndContinue(Exception e)
	{
		EAM.logException(e);
	}
	
	public void possiblyShowMissingObjectsWarningDialog() throws Exception
	{
		ORefList missingObjectRefs = findAllMissingObjects();
		if (missingObjectRefs.size() == 0 )
			return;
		
		for (int i = 0; i < missingObjectRefs.size(); ++i)
		{
			ORef missingRef = missingObjectRefs.get(i);
			ORefSet referrers = project.getObjectManager().getReferringObjects(missingRef);
			EAM.logError("Missing object: " + missingRef + " referred to by: " + referrers);
		}

// NOTE: This is appropriate for testing, but not for production
//		EAM.notifyDialog("<html>This project has some data corruption, " +
//						 "which may cause error messages or unexpected results within Miradi. <br>" +
//						 "Please contact the Miradi team to report this problem, " +
//						 "and/or to have them repair this project.");
	}
	
	public ORefList findAllMissingObjects() throws Exception
	{
		ORefList missingObjectRefs = new ORefList();
		for (int objectType = 0; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			PoolWithIdAssigner pool = (PoolWithIdAssigner) project.getPool(objectType);
			if (pool == null)
				continue;
			
			missingObjectRefs.addAll(getMissingObjectsReferredToBy(pool.getORefList()));
		}
		
		return missingObjectRefs;
	}
	
	private ORefList getMissingObjectsReferredToBy(ORefList refList) throws Exception
	{
		ORefList missingObjectRefs = new ORefList();
		for (int i = 0; i < refList.size(); ++i)
		{
			BaseObject foundObject = project.findObject(refList.get(i));
			ORefSet referredRefs = foundObject.getAllReferencedObjects();
			missingObjectRefs.addAll(extractMissingObjectRefs(referredRefs));
		}
		
		return missingObjectRefs;
	}

	private ORefList extractMissingObjectRefs(ORefSet ownedAndReferredRefs)
	{
		ORefList missingObjectRefs = new ORefList();
		for(ORef ref : ownedAndReferredRefs)
		{
			if (ref.isInvalid())
				continue;
			
			if(ref.getObjectType() == ObjectType.FAKE)
			{
				EAM.logDebug("Ref with fake type but non-invalid id: " + ref.getObjectId());
				continue;
			}
			
			if (ref.getObjectType() == ObjectType.FACTOR)
			{
				EAM.logDebug("Ref with factor type with id:" + ref.getObjectId());
				continue;
			}
			
			BaseObject foundObject = project.findObject(ref);
			if (foundObject != null)
				continue;
			
			missingObjectRefs.add(ref);
		}
		
		return missingObjectRefs;
	}
	
	public void repairDiagramObjectsReferringToNonExistantTags() throws Exception
	{
		ORefList allDiagramObjectRefs = getProject().getAllDiagramObjectRefs();
		for (int index = 0; index < allDiagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), allDiagramObjectRefs.get(index));
			removeNonExistingTagsFromSeletedTagsList(diagramObject);
		}
	}

	private void removeNonExistingTagsFromSeletedTagsList(DiagramObject diagramObject) throws Exception
	{
		ORefList existingTagRefs = new ORefList();
		ORefList selectedTagRefs = diagramObject.getSelectedTaggedObjectSetRefs();
		for (int index = 0; index < selectedTagRefs.size(); ++index)
		{
			ORef taggedObjectSetRef = selectedTagRefs.get(index);
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
			if (taggedObjectSet != null)
				existingTagRefs.add(taggedObjectSetRef);
		}
		
		diagramObject.setData(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, existingTagRefs.toString());
	}
	
	private Project getProject()
	{
		return project;
	}

	private Project project;
}
