/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.awt.Point;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.PoolWithIdAssigner;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectRepairer
{
	public static void scanForCorruptedObjects(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.possiblyShowCorruptedObjectsWarningDialog();
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
	}

	 
	
	private void repairUnsnappedNodes()
	{
		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		for (int i = 0; i < diagramFactors.length; ++i) 
			fixLocation(diagramFactors[i]);
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
	
	public void possiblyShowCorruptedObjectsWarningDialog() throws Exception
	{
		ORefList corruptedObjectRefs = findAllCorruptedObjects();
		if (corruptedObjectRefs.size() == 0 )
			return;
		
		for (int i = 0; i < corruptedObjectRefs.size(); ++i)
		{
			EAM.logError("found corrupted object: " + corruptedObjectRefs.get(i));
		}
		
		EAM.notifyDialog("<html>This project has some data corruption, " +
						 "which may cause error messages or unexpected results within Miradi. <br>" +
						 "Please contact the Miradi team to report this problem, " +
						 "and/or to have them repair this project.");
	}
	
	public ORefList findAllCorruptedObjects() throws Exception
	{
		ORefList corruptedObjectRefs = new ORefList();
		for (int objectType = 0; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			PoolWithIdAssigner pool = (PoolWithIdAssigner) project.getPool(objectType);
			if (pool == null)
				continue;
			
			corruptedObjectRefs.addAll(scanObjects(pool.getORefList()));
		}
		
		return corruptedObjectRefs;
	}
	
	private ORefList scanObjects(ORefList refList) throws Exception
	{
		ORefList corruptedObjectRefs = new ORefList();
		for (int i = 0; i < refList.size(); ++i)
		{
			BaseObject foundObject = project.findObject(refList.get(i));
			ORefList referredRefs = foundObject.getAllReferncedObjects();
			corruptedObjectRefs.addAll(findCorruptedObjects(referredRefs));
		}
		
		return corruptedObjectRefs;
	}

	private ORefList findCorruptedObjects(ORefList ownedAndReferredRefs)
	{
		ORefList corruptedObjectRefs = new ORefList();
		for (int i = 0; i < ownedAndReferredRefs.size(); ++i)
		{
			ORef ref = ownedAndReferredRefs.get(i);
			if (ref.isInvalid())
				continue;
			
			BaseObject foundObject = project.findObject(ref);
			if (foundObject != null)
				continue;
			
			corruptedObjectRefs.add(ref);
		}
		
		return corruptedObjectRefs;
	}

	private Project project;
}
