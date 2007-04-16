/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.DiagramChainObject;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectChain extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isDiagramView())
			return false;
		
		FactorCell[] selectedFactors = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		DiagramFactorLink[] selectedLinks = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		int combinedLengths = selectedLinks.length + selectedFactors.length;
		
		if (combinedLengths == 0)
			return false;
	    
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		try
		{
			DiagramView view = (DiagramView)getView();
			DiagramPanel diagram = view.getDiagramPanel();
			selectAllChainsRelatedToAllSelectedCells(diagram);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	public static void selectAllChainsRelatedToAllSelectedCells(DiagramPanel diagramPanel) throws Exception
	{
		selectChainBasedOnFactorSelection(diagramPanel);
		selectChainBasedOnLinkSelection(diagramPanel);
	}

	private static void selectChainBasedOnFactorSelection(DiagramPanel diagramPanel) throws Exception
	{
		FactorCell[] selectedFactors = diagramPanel.getOnlySelectedFactorCells();
		for(int i = 0; i < selectedFactors.length; ++i)
		{
			FactorCell selectedFactor = selectedFactors[i];
			DiagramModel model = diagramPanel.getDiagramModel();
			DiagramChainObject chainObject = new DiagramChainObject();
			chainObject.buildNormalChain(model, selectedFactor.getUnderlyingObject());
			Factor[] chainNodes = chainObject.getFactors().toNodeArray();
		
			selectFactorsInChain(diagramPanel, chainNodes);
		}
	}
	
	private static void selectChainBasedOnLinkSelection(DiagramPanel diagramPanel) throws Exception
	{
		Project project = diagramPanel.getProject();
		DiagramFactorLink[] onlySelectedLinkages = diagramPanel.getOnlySelectedLinks();
		for(int i = 0; i < onlySelectedLinkages.length; ++i)
		{
			DiagramFactorLink selectedLinkage = onlySelectedLinkages[i];
			DiagramModel diagramModel = diagramPanel.getDiagramModel();
			LinkCell cell = diagramModel.findLinkCell(selectedLinkage);
			
			DiagramChainObject upstreamChain = new DiagramChainObject();
			Factor from = project.findNode(cell.getFrom().getWrappedId());
			upstreamChain.buildUpstreamChain(diagramModel, from);
			
			DiagramChainObject downstreamChain = new DiagramChainObject();
			Factor to = project.findNode(cell.getTo().getWrappedId());
			downstreamChain.buildDownstreamChain(diagramModel, to);
			
			Factor[] upstreamFactors = upstreamChain.getFactorsArray();
			Factor[] downstreamFactors = downstreamChain.getFactorsArray();
			
			selectFactorsInChain(diagramPanel, upstreamFactors);
			selectFactorsInChain(diagramPanel, downstreamFactors);
		}
	}

	private static void selectFactorsInChain(DiagramPanel diagramPanel, Factor[] chainNodes) throws Exception
	{
		DiagramModel model = diagramPanel.getDiagramModel();
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			FactorCell nodeToSelect = model.getFactorCellByWrappedId((FactorId)chainNodes[i].getId());
			diagramPanel.getdiagramComponent().addSelectionCell(nodeToSelect);
		}
	}

}
