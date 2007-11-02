/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.ViewData;

public class DiagramObjectDeleteHelper
{
	public DiagramObjectDeleteHelper(Project projectToUse, DiagramPanel diagramPanelToUse)
	{
		project = projectToUse;
		diagramPanel = diagramPanelToUse;
	}
	
	public void deleteDiagram() throws Exception
	{
		DiagramObject diagramObject = diagramPanel.getDiagramObject();
		deleteAllDiagramFactorLinks();
		deleteAllDiagramFactors();
		deleteAllSlideReferences(diagramObject);
		clearObject(diagramObject);
		removeAsCurrentDiagram();
		deleteDiagramObject(diagramObject);
	}

	private void deleteDiagramObject(DiagramObject diagramObject) throws CommandFailedException
	{
		CommandDeleteObject deleteDiagramObject = new CommandDeleteObject(diagramObject.getRef());
		project.executeCommand(deleteDiagramObject);
	}

	private void removeAsCurrentDiagram() throws Exception, CommandFailedException
	{
		ViewData viewData = project.getCurrentViewData();
		String currentDiagramViewDataTag = diagramPanel.getDiagramSplitPane().getDiagramPageList().getCurrentDiagramViewDataTag();
		CommandSetObjectData setCurrentDiagramCommand = new CommandSetObjectData(viewData.getRef(), currentDiagramViewDataTag, ORef.INVALID);
		project.executeCommand(setCurrentDiagramCommand);
	}

	private void clearObject(DiagramObject diagramObject) throws CommandFailedException
	{
		CommandSetObjectData[] commands = diagramObject.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commands);
	}
	
	private void deleteAllSlideReferences(DiagramObject diagramObject) throws Exception
	{
		ORefList list = diagramObject.findObjectsThatReferToUs(Slide.getObjectType());
		for (int i=0; i<list.size(); ++i)
		{
			project.executeCommand(new CommandSetObjectData(list.get(i), Slide.TAG_DIAGRAM_OBJECT_REF, ORef.INVALID));
		}
	}
	
	
	private void deleteAllDiagramFactors() throws Exception
	{
		DiagramModel model = diagramPanel.getDiagramModel();
		DiagramFactor[] allDiagramFactors = model.getAllDiagramFactorsAsArray();
		
		for (int i = 0; i < allDiagramFactors.length; i++)
		{
			deleteDiagramFactorAndFactor(allDiagramFactors[i]);
		}
	}

	private void deleteDiagramFactorAndFactor(DiagramFactor diagramFactor) throws Exception
	{		
		DiagramModel diagramModel = diagramPanel.getDiagramModel();
		FactorDeleteHelper factorDeleteHelper = new FactorDeleteHelper(diagramModel);
		factorDeleteHelper.deleteFactor(diagramFactor);
	}

	private void deleteAllDiagramFactorLinks() throws Exception
	{
		DiagramModel model = diagramPanel.getDiagramModel();
		DiagramLink[] allDiagramLinks = model.getAllDiagramLinksAsArray();
		
		for (int i = 0; i < allDiagramLinks.length; i++)
		{ 
			deleteDiagramLinkAndFactorLink(allDiagramLinks[i]);
		}
	}
	
	private void deleteDiagramLinkAndFactorLink(DiagramLink diagramLink) throws Exception
	{
		FactorLinkId factorLinkId = diagramLink.getWrappedId();
		
		DiagramObject diagramObject = diagramPanel.getDiagramObject();
		CommandSetObjectData removeFactorLinkFromChain = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLink.getDiagramLinkageId());
		project.executeCommand(removeFactorLinkFromChain);
	
		Command[] commandsToClear = diagramLink.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commandsToClear);
		
		CommandDeleteObject deleteDiagramLink = new CommandDeleteObject(diagramLink.getRef());
		project.executeCommand(deleteDiagramLink);
		
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		ObjectManager objectManager = project.getObjectManager();
		ORefList referrers = factorLink.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, factorLink.getRef());
		
		if (referrers.size() > 0)
			return;
		
		Command[] commandsToClearFactorLink = factorLink.createCommandsToClear();
		project.executeCommandsWithoutTransaction(commandsToClearFactorLink);
		
		CommandDeleteObject deleteFactorLink = new CommandDeleteObject(factorLink.getRef());
		project.executeCommand(deleteFactorLink);
	}
			
	private Project project;
	private DiagramPanel diagramPanel;
}
