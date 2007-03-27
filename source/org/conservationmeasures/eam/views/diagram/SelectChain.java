/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
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
		FactorCell[] selectedFactors = getProject().getOnlySelectedFactors();
		DiagramFactorLink[] selectedLinks = getProject().getOnlySelectedLinks();
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
			DiagramComponent diagram = view.getDiagramComponent();
			selectAllChainsRelatedToAllSelectedCells(diagram);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	public static void selectAllChainsRelatedToAllSelectedCells(DiagramComponent diagram) throws Exception
	{
		selectChainBasedOnFactorSelection(diagram);
		selectChainBasedOnLinkSelection(diagram);
	}

	private static void selectChainBasedOnFactorSelection(DiagramComponent diagram) throws Exception
	{
		Project project = diagram.getProject();
		FactorCell[] selectedFactors = project.getOnlySelectedFactors();
		for(int i = 0; i < selectedFactors.length; ++i)
		{
			FactorCell selectedFactor = selectedFactors[i];
			DiagramModel model = project.getDiagramModel();
			ChainObject chainObject = new ChainObject();
			chainObject.buildNormalChain(model, selectedFactor.getUnderlyingObject());
			Factor[] chainNodes = chainObject.getFactors().toNodeArray();
		
			selectFactorsInChain(diagram, chainNodes);
		}
	}
	
	private static void selectChainBasedOnLinkSelection(DiagramComponent diagram) throws Exception
	{
		Project project = diagram.getProject();
		DiagramFactorLink[] onlySelectedLinkages = project.getOnlySelectedLinks();
		for(int i = 0; i < onlySelectedLinkages.length; ++i)
		{
			DiagramFactorLink selectedLinkage = onlySelectedLinkages[i];
			DiagramModel diagramModel = project.getDiagramModel();
			LinkCell cell = diagramModel.findLinkCell(selectedLinkage);
			
			ChainObject upstreamChain = new ChainObject();
			Factor from = project.findNode(cell.getFrom().getWrappedId());
			upstreamChain.buildUpstreamChain(diagramModel, from);
			
			ChainObject downstreamChain = new ChainObject();
			Factor to = project.findNode(cell.getTo().getWrappedId());
			downstreamChain.buildDownstreamChain(diagramModel, to);
			
			Factor[] upstreamFactors = upstreamChain.getFactorsArray();
			Factor[] downstreamFactors = downstreamChain.getFactorsArray();
			
			selectFactorsInChain(diagram, upstreamFactors);
			selectFactorsInChain(diagram, downstreamFactors);
		}
	}

	private static void selectFactorsInChain(DiagramComponent diagram, Factor[] chainNodes) throws Exception
	{
		DiagramModel model = diagram.getDiagramModel();
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			FactorCell nodeToSelect = model.getFactorCellByWrappedId((FactorId)chainNodes[i].getId());
			diagram.addSelectionCell(nodeToSelect);
		}
	}

}
