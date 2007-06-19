/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;

public class LinkDeletor
{
	public LinkDeletor()
	{
		
	}
	
	public void deleteFactorLink(DiagramObject diagramObject, DiagramLink linkageToDelete) throws Exception
	{	
		Project project = diagramObject.getProject();
		DiagramFactorLinkId id = linkageToDelete.getDiagramLinkageId();
		CommandSetObjectData removeDiagramFactorLink = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, id);
		project.executeCommand(removeDiagramFactorLink);
		
		Command[] commandsToClearDiagramLink = linkageToDelete.createCommandsToClear();
		project.executeCommands(commandsToClearDiagramLink);
		
		CommandDeleteObject removeFactorLinkCommand = new CommandDeleteObject(ObjectType.DIAGRAM_LINK, id);
		project.executeCommand(removeFactorLinkCommand);

		if (!canDeleteFactorLink(project, linkageToDelete))
				return;

		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId()).createCommandsToClear();
		project.executeCommands(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId());
		project.executeCommand(deleteLinkage);
	}

	private boolean canDeleteFactorLink(Project project, DiagramLink linkageToDelete)
	{
		ObjectManager objectManager = project.getObjectManager();
		FactorLinkId factorLinkId = linkageToDelete.getWrappedId();
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		ORefList referrers = factorLink.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, factorLink.getRef());
		if (referrers.size() > 0)
			return false;
		
		return true;
	}

}
