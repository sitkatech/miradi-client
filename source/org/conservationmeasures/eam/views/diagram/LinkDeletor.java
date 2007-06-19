/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.FactorLinkId;
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

	public void deleteFactorLink(FactorLinkId factorLinkId) throws Exception
	{
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		deleteFactorLink(factorLink);
	}
	
	public void deleteFactorLink(FactorLink factorLink) throws Exception
	{
		deleteAllReffererDiagramLinks(factorLink);
		
		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, factorLink.getId()).createCommandsToClear();
		project.executeCommands(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, factorLink.getId());
		project.executeCommand(deleteLinkage);
	}
	
	
	private void deleteAllReffererDiagramLinks(FactorLink link) throws Exception
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList diagramLinkreferrers = link.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, link.getRef());
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

	private Project project;
}
