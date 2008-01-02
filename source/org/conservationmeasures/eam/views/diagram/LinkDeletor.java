/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;

public class LinkDeletor
{
	public LinkDeletor(Project projectToUse)
	{
		project = projectToUse;
	}

	public void deleteFactorLinkAndAllRefferers(FactorLinkId factorLinkId) throws Exception
	{
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		deleteDiagramLinkAndReferrers(factorLink);
		deleteFactorLinkIfOrphaned(factorLink);
	}
	
	private void deleteDiagramLinkAndReferrers(FactorLink factorLink) throws Exception
	{
		ORefList diagramLinkreferrers = factorLink.findObjectsThatReferToUs(DiagramLink.getObjectType());	
		for (int referrerIndex = 0; referrerIndex < diagramLinkreferrers.size(); ++referrerIndex)
		{
			DiagramLink diagramLink = (DiagramLink) project.findObject(diagramLinkreferrers.get(referrerIndex));
			deleteOurGroupDiagramLinkParents(diagramLink);
			deleteDiagramLink(diagramLink);
		}
	}

	private void deleteOurGroupDiagramLinkParents(DiagramLink diagramLink) throws Exception
	{
		ORefList groupBoxDiagramLinkReferrers = diagramLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int groupLinkIndex = 0; groupLinkIndex < groupBoxDiagramLinkReferrers.size(); ++groupLinkIndex)
		{
			DiagramLink groupDiagramLink = DiagramLink.find(getProject(), groupBoxDiagramLinkReferrers.get(groupLinkIndex));
			if (groupDiagramLink.alsoLinksOurFromOrTo(diagramLink))
				deleteDiagramLink(groupDiagramLink);
		}
	}
	
	public void deleteFactorLinkAndDiagramLink(ORefList factorsAboutToBeDeleted, DiagramLink diagramLink) throws Exception
	{
		FactorLink factorLink = diagramLink.getUnderlyingLink();
		deleteDiagramLink(diagramLink);
		if (!isToOrFromFactorBeingDeleted(factorsAboutToBeDeleted, factorLink))
			deleteAllReferrerDiagramLinks(factorLink);

		deleteFactorLinkIfOrphaned(factorLink);
	}

	public void deleteFactorLinksAndGroupBoxDiagramLinks(ORefList factorsAboutToBeDeleted, DiagramLink diagramLink) throws Exception
	{
		ORefList groupBoxLinkChildRefs = diagramLink.getGroupedDiagramLinkRefs();
		deleteDiagramLink(diagramLink);
		
		for (int i = 0; i < groupBoxLinkChildRefs.size(); ++i)
		{
			DiagramLink childDiagramLink = DiagramLink.find(getProject(), groupBoxLinkChildRefs.get(i));
			deleteFactorLinkAndDiagramLink(factorsAboutToBeDeleted, childDiagramLink);
		}
	}
	
	private void deleteAllReferrerDiagramLinks(FactorLink factorLink) throws Exception
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList diagramLinkreferrers = factorLink.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, factorLink.getRef());
		deleteDiagramLinks(diagramLinkreferrers);
	}

	private void deleteDiagramLinks(ORefList diagramLinkORefs) throws Exception
	{
		for (int i = 0; i < diagramLinkORefs.size(); ++i)
		{
			DiagramLink diagramLink = (DiagramLink) project.findObject(diagramLinkORefs.get(i));
			deleteDiagramLink(diagramLink);
		}
	}
	
	public void deleteDiagramLink(DiagramLink diagramLink) throws Exception
	{
		BaseObject owner = diagramLink.getOwner();
		DiagramObject diagramObject = (DiagramObject) owner;
		
		removeFromGroupBoxDiagramLinkChildren(diagramLink);
		
		CommandSetObjectData removeDiagramFactorLink = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLink.getDiagramLinkageId());
		project.executeCommand(removeDiagramFactorLink);

		Command[] commandsToClearDiagramLink = diagramLink.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commandsToClearDiagramLink);

		CommandDeleteObject removeFactorLinkCommand = new CommandDeleteObject(diagramLink.getRef());
		project.executeCommand(removeFactorLinkCommand);
	}

	private void removeFromGroupBoxDiagramLinkChildren(DiagramLink diagramLink) throws Exception
	{
		ORefList diagramBoxDiagramLinkReferrerRefs = diagramLink.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int i = 0; i < diagramBoxDiagramLinkReferrerRefs.size(); ++i)
		{
			DiagramLink groupBoxLink = DiagramLink.find(getProject(), diagramBoxDiagramLinkReferrerRefs.get(i));
			CommandSetObjectData removeDiagramLink = CommandSetObjectData.createRemoveORefCommand(groupBoxLink, diagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, diagramLink.getRef());
			getProject().executeCommand(removeDiagramLink);
		}
	}
	
	private void deleteFactorLinkIfOrphaned(FactorLink factorLink) throws CommandFailedException
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList diagramLinkReferrers = factorLink.findObjectsThatReferToUs(objectManager, DiagramLink.getObjectType(), factorLink.getRef());
		
		if (diagramLinkReferrers.size() != 0)
			return;
		
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		
		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, factorLink.getId()).createCommandsToClear();
		project.executeCommandsWithoutTransaction(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, factorLink.getId());
		project.executeCommand(deleteLinkage);
		
		deleteOrphanedThreatStressRatings(threatStressRatingRefs);
	}
	
	private void deleteOrphanedThreatStressRatings(ORefList threatStressRatingRefs) throws CommandFailedException
	{
		for (int i = 0; i < threatStressRatingRefs.size(); ++i)
		{
			ORef threatStressRatingRef = threatStressRatingRefs.get(i);
			ThreatStressRating threatStressRating = (ThreatStressRating) project.findObject(threatStressRatingRef);
			ORefList allReferrers = threatStressRating.findObjectsThatReferToUs();
			if (allReferrers.size() != 0)
				continue;
			
			deleteThreatStressRating(threatStressRating);
		}
	}

	public void deleteThreatStressRating(ThreatStressRating threatStressRating) throws CommandFailedException
	{
		Command[] commandsToClear = threatStressRating.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commandsToClear);
		
		CommandDeleteObject deleteThreatStressRating = new CommandDeleteObject(threatStressRating.getRef());
		project.executeCommand(deleteThreatStressRating);
	}

	private boolean isToOrFromFactorBeingDeleted(ORefList factorsAboutToBeDeleted, FactorLink factorLink)
	{
		for (int i = 0; i < factorsAboutToBeDeleted.size(); ++i)
		{
			ORef factorRefToBeDeleted = factorsAboutToBeDeleted.get(i);
			ORef toRef = factorLink.getToFactorRef();
			ORef fromRef = factorLink.getFromFactorRef();
			if (toRef.equals(factorRefToBeDeleted) || fromRef.equals(factorRefToBeDeleted))
				return true;
		}
		
		return false;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
