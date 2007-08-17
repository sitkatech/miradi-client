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
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;

public class LinkDeletor
{
	public LinkDeletor(Project projectToUse)
	{
		project = projectToUse;
	}

	public void deleteFactorLink(DiagramFactorLinkId diagramFactorLinkId, ORefList factorsAboutToBeDeleted) throws Exception
	{
		DiagramLink diagramLink = (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramFactorLinkId));
		deleteDiagramLink(factorsAboutToBeDeleted, diagramLink);
	}
	
	public void deleteFactorLinkAndAllRefferers(FactorLinkId factorLinkId) throws Exception
	{
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		deleteAllReferrerDiagramLinks(factorLink);
	}
	
	private void deleteDiagramLink(ORefList factorsAboutToBeDeleted, DiagramLink diagramLink) throws Exception
	{
		deleteDiagramLink(diagramLink);
		possiblyDeleteAllReffererDiagramLinks(factorsAboutToBeDeleted, diagramLink);
		deleteFactorLink(diagramLink.getUnderlyingLink());
	}

	private void possiblyDeleteAllReffererDiagramLinks(ORefList factorsAboutToBeDeleted, DiagramLink diagramLink) throws Exception
	{
		FactorLink factorLink = diagramLink.getUnderlyingLink();
		if (hasToFromFactorsThatWillBeDeleted(factorsAboutToBeDeleted, factorLink))
			return;
		
		deleteAllReferrerDiagramLinks(factorLink);
	}

	private void deleteAllReferrerDiagramLinks(FactorLink factorLink) throws Exception
	{
		notifyUserOfAllReferringLinksBeingDeleted();		
		ObjectManager objectManager = project.getObjectManager();
		ORefList diagramLinkreferrers = factorLink.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, factorLink.getRef());
		deleteDiagramLinks(diagramLinkreferrers);
	}

	private void notifyUserOfAllReferringLinksBeingDeleted()
	{
		EAM.notifyDialog(EAM.text("As a result of this delete, all diagram" +
				" links that share the same to and from factors in" +
				" all the conceptual model pages will be deleted."));
	}

	private void deleteDiagramLinks(ORefList diagramLinkORefs) throws Exception
	{
		for (int i = 0; i < diagramLinkORefs.size(); ++i)
		{
			DiagramLink diagramLink = (DiagramLink) project.findObject(diagramLinkORefs.get(i));
			deleteDiagramLink(diagramLink);
		}
	}
	
	private void deleteDiagramLink(DiagramLink diagramLink) throws Exception
	{
		BaseObject owner = diagramLink.getOwner();
		DiagramObject diagramObject = (DiagramObject) owner;
		CommandSetObjectData removeDiagramFactorLink = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLink.getDiagramLinkageId());
		project.executeCommand(removeDiagramFactorLink);

		Command[] commandsToClearDiagramLink = diagramLink.createCommandsToClear();
		project.executeCommands(commandsToClearDiagramLink);

		CommandDeleteObject removeFactorLinkCommand = new CommandDeleteObject(ObjectType.DIAGRAM_LINK, diagramLink.getDiagramLinkageId());
		project.executeCommand(removeFactorLinkCommand);
	}
	
	private void deleteFactorLink(FactorLink factorLink) throws CommandFailedException
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList diagramFactorReferrers = factorLink.findObjectsThatReferToUs(objectManager, DiagramLink.getObjectType(), factorLink.getRef());
		
		if (diagramFactorReferrers.size() != 0)
			return;
		
		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, factorLink.getId()).createCommandsToClear();
		project.executeCommands(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, factorLink.getId());
		project.executeCommand(deleteLinkage);
	}
	
	private boolean hasToFromFactorsThatWillBeDeleted(ORefList factorsAboutToBeDeleted, FactorLink factorLink)
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

	private Project project;
}
