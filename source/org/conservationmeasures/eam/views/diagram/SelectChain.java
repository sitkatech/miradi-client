/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectChain extends ViewDoer
{
	public boolean isAvailable()
	{
		DiagramFactor[] selectedFactors = getProject().getOnlySelectedFactors();
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
		DiagramFactor[] selectedFactors = project.getOnlySelectedFactors();
		for(int i = 0; i < selectedFactors.length; ++i)
		{
			DiagramFactor selectedFactor = selectedFactors[i];
			DiagramModel model = project.getDiagramModel();
			ChainObject chainObject = new ChainObject();
			chainObject.buildNormalChain(model, selectedFactor.getUnderlyingObject());
			Factor[] chainNodes = chainObject.getFactors().toNodeArray();
			FactorLink[] linksInChain = chainObject.getFactorLinksArray();
		
			selectLinksInChain(diagram, linksInChain);
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
			
			FactorLink[] downstreamLinks = downstreamChain.getFactorLinksArray();
			FactorLink[] upstreamLinks = upstreamChain.getFactorLinksArray();
			Factor[] upstreamFactors = upstreamChain.getFactorsArray();
			Factor[] downstreamFactors = downstreamChain.getFactorsArray();
			
			selectFactorsInChain(diagram, upstreamFactors);
			selectFactorsInChain(diagram, downstreamFactors);
			
			selectLinksInChain(diagram, upstreamLinks);
			selectLinksInChain(diagram, downstreamLinks);
		}
	}

	private static void selectFactorsInChain(DiagramComponent diagram, Factor[] chainNodes) throws Exception
	{
		DiagramModel model = diagram.getDiagramModel();
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			DiagramFactor nodeToSelect = model.getDiagramFactorByWrappedId((FactorId)chainNodes[i].getId());
			diagram.addSelectionCell(nodeToSelect);
		}
	}

	private static void selectLinksInChain(DiagramComponent diagram, FactorLink[] linksInChain) throws Exception
	{
		DiagramModel model = diagram.getDiagramModel();
		for (int i = 0 ; i < linksInChain.length; i++)
		{
			DiagramFactorLink linkToSelect = model.getDiagramFactorLinkbyWrappedId((FactorLinkId)linksInChain[i].getId());
			diagram.addSelectionCell(model.findLinkCell(linkToSelect));
		}
	}

}
