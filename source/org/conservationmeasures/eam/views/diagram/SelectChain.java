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
		DiagramFactor[] selectedFactors = getProject().getOnlySelectedFactors();
		DiagramFactorLink[] selectedLinks = getProject().getOnlySelectedLinks();
		int combinedLengths = selectedLinks.length + selectedFactors.length;
		
		if (combinedLengths != 1)
			return false;
	    
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		try
		{
			if (getProject().getOnlySelectedFactors().length == 1)
				selectChainBasedOnFactorSelection();
			else if (getProject().getOnlySelectedLinks().length == 1)
				selectChainBasedOnLinkSelection();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	private void selectChainBasedOnFactorSelection() throws Exception
	{
		DiagramFactor selectedFactor = getProject().getOnlySelectedFactors()[0];
		DiagramModel model = getProject().getDiagramModel();
		ChainObject chainObject = new ChainObject();
		chainObject.buildNormalChain(model, selectedFactor.getUnderlyingObject());
		Factor[] chainNodes = chainObject.getFactors().toNodeArray();
		FactorLink[] linksInChain = chainObject.getFactorLinksArray();
	
		selectLinksInChain(model, linksInChain);
		selectFactorsInChain(model, chainNodes);
	}
	
	private void selectChainBasedOnLinkSelection() throws Exception
	{
		DiagramFactorLink[] onlySelectedLinkages = getProject().getOnlySelectedLinks();
		DiagramFactorLink selectedLinkage = onlySelectedLinkages[0];
		DiagramModel diagramModel = getProject().getDiagramModel();
		
		ChainObject upstreamChain = new ChainObject();
		Factor from = getProject().findNode(selectedLinkage.getFromFactorId());
		upstreamChain.buildUpstreamChain(diagramModel, from);
		
		ChainObject downstreamChain = new ChainObject();
		Factor to = getProject().findNode(selectedLinkage.getToFactorId());
		downstreamChain.buildDownstreamChain(diagramModel, to);
		
		FactorLink[] downstreamLinks = downstreamChain.getFactorLinksArray();
		FactorLink[] upstreamLinks = upstreamChain.getFactorLinksArray();
		Factor[] upstreamFactors = upstreamChain.getFactorsArray();
		Factor[] downstreamFactors = downstreamChain.getFactorsArray();
		
		selectFactorsInChain(diagramModel, upstreamFactors);
		selectFactorsInChain(diagramModel, downstreamFactors);
		
		selectLinksInChain(diagramModel, upstreamLinks);
		selectLinksInChain(diagramModel, downstreamLinks);
	}

	private void selectFactorsInChain(DiagramModel model, Factor[] chainNodes) throws Exception
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
			view.getDiagramComponent().addSelectionCell(model.findLinkCell(linkToSelect));
		}
	}

}
