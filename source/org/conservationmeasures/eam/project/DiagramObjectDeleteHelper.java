/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.diagram.LinkDeletor;

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
		LinkDeletor linkDeletor = new LinkDeletor(project);
		
		ORefList allFactors = getAllFactorsToBeDeleted(model);
		for (int i = 0; i < allDiagramLinks.length; i++)	
		{ 
			deletDiagramLink(linkDeletor, allFactors, allDiagramLinks[i]);
		}
	}

	private void deletDiagramLink(LinkDeletor linkDeletor, ORefList allFactors, DiagramLink diagramLink) throws Exception
	{
		DiagramLink found = DiagramLink.find(project, diagramLink.getRef());
		if (found == null)
			return;
		
		if (diagramLink.isGroupBoxLink())
			linkDeletor.deleteFactorLinksAndGroupBoxDiagramLinks(allFactors, diagramLink);
		else
			linkDeletor.deleteFactorLinkAndDiagramLink(allFactors, diagramLink);
	}

	private ORefList getAllFactorsToBeDeleted(DiagramModel model)
	{
		DiagramFactor[] allDiagramFactors = model.getAllDiagramFactorsAsArray();
		ORefList factorRefs = new ORefList();
		for (int i = 0 ; i < allDiagramFactors.length; ++i)
		{
			factorRefs.add(allDiagramFactors[i].getWrappedORef());
		}
		
		return factorRefs;
	}
	
	private Project project;
	private DiagramPanel diagramPanel;
}
