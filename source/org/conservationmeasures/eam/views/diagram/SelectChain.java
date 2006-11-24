/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectChain extends ViewDoer
{
	public boolean isAvailable()
	{
		DiagramFactor[] selectedNodes = getProject().getOnlySelectedNodes();
		DiagramFactorLink[] selectedLinkages = getProject().getOnlySelectedLinkages();
		int combinedLengths = selectedLinkages.length + selectedNodes.length;
		
		if (combinedLengths != 1)
			return false;
	    
		return (isNode(selectedNodes) || isLinkage(selectedLinkages));
	}
	
	private boolean isNode(DiagramFactor[] selectedNodes)
	{
		boolean isNode = false;
		if (selectedNodes.length == 1)
			isNode = selectedNodes[0].isFactor();
		return isNode;
	}

	private boolean isLinkage(DiagramFactorLink[] selectedLinkages)
	{
		boolean isLinkage  = false;
		if (selectedLinkages.length == 1 )
			isLinkage = selectedLinkages[0].isFactorLink();
		
		return isLinkage;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		try
		{
			if (getProject().getOnlySelectedNodes().length == 1)
				selectChainBasedOnNodeSelection();
			else if (getProject().getOnlySelectedLinkages().length == 1)
				selectChainBasedOnLinkageSelection();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	private void selectChainBasedOnNodeSelection() throws Exception
	{
		DiagramFactor selectedNode = getProject().getOnlySelectedNodes()[0];
		DiagramModel model = getProject().getDiagramModel();
		ChainObject chainObject = new ChainObject();
		chainObject.buildNormalChain(model, selectedNode.getUnderlyingObject());
		Factor[] chainNodes = chainObject.getFactors().toNodeArray();
		FactorLink[] linksInChain = chainObject.getFactorLinksArray();
	
		selectLinksInChain(model, linksInChain);
		selectNodesInChain(model, chainNodes);
	}
	
	private void selectChainBasedOnLinkageSelection() throws Exception
	{
		DiagramFactorLink[] onlySelectedLinkages = getProject().getOnlySelectedLinkages();
		DiagramFactorLink selectedLinkage = onlySelectedLinkages[0];
		DiagramModel diagramModel = getProject().getDiagramModel();
		
		ChainObject upstreamChain = new ChainObject();
		upstreamChain.buildUpstreamChain(diagramModel, selectedLinkage.getFromNode().getUnderlyingObject());
		
		ChainObject downstreamChain = new ChainObject();
		downstreamChain.buildDownstreamChain(diagramModel, selectedLinkage.getToNode().getUnderlyingObject());
		
		FactorLink[] downLinkages = downstreamChain.getFactorLinksArray();
		FactorLink[] upLinkages = upstreamChain.getFactorLinksArray();
		Factor[] upNodes = upstreamChain.getFactorsArray();
		Factor[] downNodes = downstreamChain.getFactorsArray();
		
		selectNodesInChain(diagramModel, upNodes);
		selectNodesInChain(diagramModel, downNodes);
		
		selectLinksInChain(diagramModel, upLinkages);
		selectLinksInChain(diagramModel, downLinkages);
	}

	private void selectNodesInChain(DiagramModel model, Factor[] chainNodes) throws Exception
	{
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			DiagramFactor nodeToSelect = model.getDiagramFactorByWrappedId((FactorId)chainNodes[i].getId());
			DiagramView view = (DiagramView)getView();
			view.getDiagramComponent().addSelectionCell(nodeToSelect);
		}
	}

	private void selectLinksInChain(DiagramModel model, FactorLink[] linksInChain) throws Exception
	{
		for (int i = 0 ; i < linksInChain.length; i++)
		{
			DiagramFactorLink linkToSelect = model.getDiagramFactorLinkbyWrappedId((FactorLinkId)linksInChain[i].getId());
			DiagramView view = (DiagramView)getView();
			view.getDiagramComponent().addSelectionCell(linkToSelect);
		}
	}

}
