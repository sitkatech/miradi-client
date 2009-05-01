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

import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.PoolWithIdAssigner;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.TableSettings;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatStressRating;
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
		if (ProjectServer.DATA_VERSION <= DATA_VERSION_NON_EXISTANT_TAG_REFS_IN_DIAGRAM_OBJECT_FIXED)
			repairDiagramObjectsReferringToNonExistantTags();
		
		if (ProjectServer.DATA_VERSION <= DATA_VERSION_NON_EXISTANT_TAG_REFS_IN_DIAGRAM_OBJECT_FIXED)
			repairAssignmentsReferringToNonExistantData();
		
		if (ProjectServer.DATA_VERSION <= DATA_VERSION_WITH_POSSIBLE_LINKED_TEXT_BOXES)
			repairLinkedTextBoxes();
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
	
	private void possiblyShowMissingObjectsWarningDialog() throws Exception
	{
		ORefList missingObjectRefs = findAllMissingObjects();
		if (missingObjectRefs.size() == 0 )
			return;
		
		for (int i = 0; i < missingObjectRefs.size(); ++i)
		{
			ORef missingRef = missingObjectRefs.get(i);
			ORefSet referrers = project.getObjectManager().getReferringObjects(missingRef);
			if (hasOnlyTableSettingReferrers(referrers))
				continue;
			
			EAM.logError("Missing object: " + missingRef + " referred to by: " + referrers);
		}
		
		detectAndReportOrphans(FactorLink.getObjectType(), DiagramLink.getObjectType());
		detectAndReportOrphans(ThreatStressRating.getObjectType(), FactorLink.getObjectType());
		detectAndReportOrphans(Cause.getObjectType(), DiagramFactor.getObjectType());

// NOTE: This is appropriate for testing, but not for production
//		EAM.notifyDialog("<html>This project has some data corruption, " +
//						 "which may cause error messages or unexpected results within Miradi. <br>" +
//						 "Please contact the Miradi team to report this problem, " +
//						 "and/or to have them repair this project.");
	}

	private boolean hasOnlyTableSettingReferrers(ORefSet referrers)
	{
		for(ORef ref : referrers)
		{
			if (!TableSettings.is(ref))
				return false;
		}
		
		return true;
	}

	private void detectAndReportOrphans(int possibleOrphanType,
			final int custodianType)
	{
		ORefList possibleOrphanRefs = getProject().getObjectManager().getPool(possibleOrphanType).getORefList();
		for(int i = 0; i < possibleOrphanRefs.size(); ++i)
		{
			final ORef possibleOrphanRef = possibleOrphanRefs.get(i);
			BaseObject possibleOrphan = BaseObject.find(getProject(), possibleOrphanRef);
			ORefList custodianRefs = possibleOrphan.findObjectsThatReferToUs(custodianType);
			if(custodianRefs.size() == 0)
				EAM.logError(possibleOrphan.getTypeName() + " without custodian: " + possibleOrphanRef);
		}
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
		ORefList nonDeletedTagRefs = new ORefList();
		ORefList selectedTagRefs = diagramObject.getSelectedTaggedObjectSetRefs();
		for (int index = 0; index < selectedTagRefs.size(); ++index)
		{
			ORef taggedObjectSetRef = selectedTagRefs.get(index);
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
			if (taggedObjectSet != null)
				nonDeletedTagRefs.add(taggedObjectSetRef);
		}
		if (nonDeletedTagRefs.size() != selectedTagRefs.size())
			getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, nonDeletedTagRefs.toString());
	}
	
	public void repairAssignmentsReferringToNonExistantData() throws Exception
	{
		ORefList allAssignmentRefs = getProject().getAssignmentPool().getRefList();
		for (int index = 0; index < allAssignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.find(getProject(), allAssignmentRefs.get(index));
			possiblyClearField(assignment, assignment.getAccountingCodeRef(), Assignment.TAG_ACCOUNTING_CODE);
			possiblyClearField(assignment, assignment.getFundingSourceRef(), Assignment.TAG_FUNDING_SOURCE);
		}
	}
	
	private void possiblyClearField(Assignment assignment, ORef refToTestForExistance, String tagToClear) throws Exception
	{
		BaseObject foundObject = getProject().findObject(refToTestForExistance);
		if (foundObject == null)
			getProject().setObjectData(assignment.getRef(), tagToClear, BaseId.INVALID.toString());
	}
	
	public void repairLinkedTextBoxes() throws Exception
	{
		ORefList diagramLinkRefs = getProject().getDiagramFactorLinkPool().getORefList();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
			DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
			if (TextBox.is(fromDiagramFactor.getWrappedORef()) || TextBox.is(toDiagramFactor.getWrappedORef()))
			{
				removeFromAllDiagramObjects(diagramLink);
				ORef refForDiagramLinkToBeDeleted = diagramLink.getRef();
				getProject().deleteObject(diagramLink);
				FactorLink factorLink = diagramLink.getWrappedFactorLink();
				
				EAM.logDebug("A diagram link to a text box was deleted " + refForDiagramLinkToBeDeleted);
				if (factorLink != null)
					getProject().deleteObject(factorLink);
			}
		}
	}

	private void removeFromAllDiagramObjects(DiagramLink diagramLink) throws Exception
	{
		ORefList diagramObjectReferrerRefs = diagramLink.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType());
		diagramObjectReferrerRefs.addAll(diagramLink.findObjectsThatReferToUs(ResultsChainDiagram.getObjectType()));
		for (int index = 0; index < diagramObjectReferrerRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectReferrerRefs.get(index));
			ORefList diagramLinkRefs = new ORefList(diagramObject.getAllDiagramLinkRefs());
			if (diagramLinkRefs.contains(diagramLink.getRef()))
			{
				diagramLinkRefs.remove(diagramLink.getRef());
				IdList diagramLinkIds = diagramLinkRefs.convertToIdList(DiagramLink.getObjectType());
				getProject().setObjectData(diagramObject.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
			}
		}
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	
	private static final int DATA_VERSION_NON_EXISTANT_TAG_REFS_IN_DIAGRAM_OBJECT_FIXED = 36;
	private static final int DATA_VERSION_WITH_POSSIBLE_LINKED_TEXT_BOXES = 36;
}
