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
import java.util.HashMap;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatStressRatingEnsurer;
import org.miradi.objectpools.ObjectPool;
import org.miradi.objectpools.PoolWithIdAssigner;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectRepairer
{
	public static HashMap<ORef, ORefSet> scanForMissingObjects(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		return  repairer.getListOfMissingObjects();
	}
	
	public static void repairProblemsWherePossible(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.repairProblemsWherePossible();
	}
	
	public static void scanForOrphans(Project project) throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		repairer.logOrphansAndSimilarProblems();
	}
	
	public ProjectRepairer(Project projectToRepair)
	{
		project = projectToRepair;
	}
	
	void repairProblemsWherePossible() throws Exception
	{
		fixAnyProblemsWithThreatStressRatings();
		repairUnsnappedNodes();
		removeInvalidDiagramLinkRefs();
	}

	private void removeInvalidDiagramLinkRefs() throws Exception
	{
		removeInvalidDiagramLinkRefs(project.getConceptualModelDiagramPool());
		removeInvalidDiagramLinkRefs(project.getResultsChainDiagramPool());
	}

	private void removeInvalidDiagramLinkRefs(ObjectPool diagramPool) throws Exception
	{
		ORefList diagramRefs = diagramPool.getRefList();
		for(int i = 0; i < diagramRefs.size(); ++i)
		{
			DiagramObject diagram = DiagramObject.findDiagramObject(getProject(), diagramRefs.get(i));
			removeInvalidDiagramLinkRefs(diagram);
		}
	}

	private void removeInvalidDiagramLinkRefs(DiagramObject diagram) throws Exception
	{
		IdList diagramLinkIds = new IdList(diagram.getAllDiagramFactorLinkIds());
		while(diagramLinkIds.contains(BaseId.INVALID))
			diagramLinkIds.removeId(BaseId.INVALID);
		
		if(diagramLinkIds.size() < diagram.getAllDiagramFactorLinkIds().size())
		{
			EAM.logWarning("Deleting -1 diagram link ids from " + diagram.getRef());
			getProject().setObjectData(diagram, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
		}
	}

	public void logOrphansAndSimilarProblems()
	{
		detectAndReportOrphans(FactorLink.getObjectType(), DiagramLink.getObjectType());
		detectAndReportOrphans(Cause.getObjectType(), DiagramFactor.getObjectType());

		warnOfOrphanDiagramFactors();
		warnOfOrphanAnnotations();	
		warnOfOrphanTasks();
		warnOfFactorsWithoutReferringDiagramFactors();
	}
	 
	private void warnOfOrphanDiagramFactors()
	{
		ORefSet possibleOrphanRefs = getProject().getPool(DiagramFactor.getObjectType()).getRefSet();
		int[] diagramTypes = new int[] {ConceptualModelDiagram.getObjectType(), ResultsChainDiagram.getObjectType()};
		ORefSet orphanRefs = getActualOrphanRefs(possibleOrphanRefs, diagramTypes);
		if (orphanRefs.hasData())
			EAM.logError("NOTE: " + orphanRefs.size() + " DiagramFactors are not in any diagram:" + orphanRefs.toRefList());
	}

	private void warnOfFactorsWithoutReferringDiagramFactors()
	{
		ORefSet factorsWithoutDiagramFactors = getFactorsWithoutDiagramFactors();
		if (factorsWithoutDiagramFactors.hasData())
			EAM.logError("WARNING: Factors are not covered by a diagramFactor:" + factorsWithoutDiagramFactors.toRefList());
	}

	private ORefSet getFactorsWithoutDiagramFactors()
	{
		ORefSet factorsWithoutDiagramFactors = new ORefSet();
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(ScopeBox.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(Target.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(HumanWelfareTarget.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(Cause.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(Strategy.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(ThreatReductionResult.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(IntermediateResult.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(GroupBox.getObjectType()));
		factorsWithoutDiagramFactors.addAll(getFactorsWithoutDiagramFactors(TextBox.getObjectType()));
		
		return factorsWithoutDiagramFactors;
	}

	public ORefSet getFactorsWithoutDiagramFactors(int factorType)
	{
		ORefSet possibleOrphanRefs = getProject().getPool(factorType).getRefSet();
		int referringObjectType = DiagramFactor.getObjectType();

		return getActualOrphanRefs(possibleOrphanRefs, new int[] {referringObjectType});
	}

	private ORefSet getActualOrphanRefs(ORefSet possibleOrphanRefs,	int[] referringObjectTypes)
	{
		ORefSet orphanRefs = new ORefSet();
		for(ORef possibleOrphanRef : possibleOrphanRefs)
		{
			BaseObject possibleOrphan = BaseObject.find(getProject(), possibleOrphanRef);
			ORefList referrerRefs = new ORefList(); 
			for(int typeIndex = 0; typeIndex < referringObjectTypes.length; ++typeIndex)
				referrerRefs.addAll(possibleOrphan.findObjectsThatReferToUs(referringObjectTypes[typeIndex]));
			
			if (referrerRefs.isEmpty())
				orphanRefs.addRef(possibleOrphan);
		}
		
		return orphanRefs;
	}

	private void warnOfOrphanTasks()
	{
		int type = Task.getObjectType();
		ORefList refs = getProject().getPool(type).getRefList();
		for(int i = 0; i < refs.size(); ++i)
		{
			BaseObject object = BaseObject.find(getProject(), refs.get(i));
			if(object.getOwnerRef().isInvalid())
				EAM.logWarning("NOTE: Object without owner! " + object.getRef());
		}
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
	
	private void warnOfOrphanAnnotations()
	{
		warnOfOrphanAnnotations(ObjectType.OBJECTIVE);
		warnOfOrphanAnnotations(ObjectType.GOAL);
		warnOfOrphanAnnotations(ObjectType.INDICATOR);
		warnOfOrphanAnnotations(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}

	private void warnOfOrphanAnnotations(int annotationType)
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
					EAM.logWarning("NOTE: Found orphan " + annotationType + ":" + annotationId);
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
	
	private void fixAnyProblemsWithThreatStressRatings() throws Exception
	{
		deleteThreatStressRatingsWithInvalidRefs();
		
		ThreatStressRatingEnsurer ensurer = new ThreatStressRatingEnsurer(getProject());
		ensurer.createOrDeleteThreatStressRatingsAsNeeded();
	}

	private void deleteThreatStressRatingsWithInvalidRefs() throws Exception
	{
		ORefSet deletedRefs = new ORefSet();
		ORefList ratingObjectRefs = getProject().getThreatStressRatingPool().getRefList();
		for(int i = 0; i < ratingObjectRefs.size(); ++i)
		{
			ThreatStressRating rating = ThreatStressRating.find(getProject(), ratingObjectRefs.get(i));
			if(rating.getThreatRef().isInvalid() || rating.getStressRef().isInvalid())
			{
				deletedRefs.add(rating.getRef());
				getProject().deleteObject(rating);
			}
		}
		
		if(deletedRefs.size() > 0)
			EAM.logWarning("Deleted " + deletedRefs.size() + " TSR's with invalid refs");
	}

	private HashMap<ORef, ORefSet> getListOfMissingObjects() throws Exception
	{
		HashMap<ORef, ORefSet> missingObjectsAndReferrers = new HashMap<ORef, ORefSet>();

		ORefList missingObjectRefs = findAllMissingObjects();
		for (int i = 0; i < missingObjectRefs.size(); ++i)
		{
			ORef missingRef = missingObjectRefs.get(i);
			ORefSet referrers = project.getObjectManager().getReferringObjects(missingRef);
			referrers = getOnlyReferrersThatMatter(referrers);
			
			if(referrers.size() > 0)
				missingObjectsAndReferrers.put(missingRef, referrers);
		}
		
		return missingObjectsAndReferrers;
	}

	private ORefSet getOnlyReferrersThatMatter(ORefSet referrers)
	{
		ORefSet filteredReferrers = new ORefSet();
		for(ORef ref : referrers)
		{
			if(TableSettings.is(ref))
				continue;
			if(ViewData.is(ref))
				continue;
			
			filteredReferrers.add(ref);
		}
		
		return filteredReferrers;
	}

	private void detectAndReportOrphans(int possibleOrphanType,	final int custodianType)
	{
		ORefList possibleOrphanRefs = getProject().getObjectManager().getPool(possibleOrphanType).getORefList();
		for(int i = 0; i < possibleOrphanRefs.size(); ++i)
		{
			final ORef possibleOrphanRef = possibleOrphanRefs.get(i);
			BaseObject possibleOrphan = BaseObject.find(getProject(), possibleOrphanRef);
			ORefList custodianRefs = possibleOrphan.findObjectsThatReferToUs(custodianType);
			if(custodianRefs.size() == 0)
				EAM.logError("NOTE: " + possibleOrphan.getTypeName() + " without custodian: " + possibleOrphanRef);
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
				EAM.logDebug("WARNING: Ref with fake type but non-invalid id: " + ref.getObjectId());
				continue;
			}
			
			if (ref.getObjectType() == ObjectType.FACTOR)
			{
				EAM.logDebug("WARNING: Ref with factor type with id:" + ref.getObjectId());
				continue;
			}
			
			BaseObject foundObject = project.findObject(ref);
			if (foundObject != null)
				continue;
			
			missingObjectRefs.add(ref);
		}
		
		return missingObjectRefs;
	}
	
	private Project getProject()
	{
		return project;
	}

	private Project project;
}
