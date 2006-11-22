/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.ChainObject;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectChain extends ViewDoer
{
	public boolean isAvailable()
	{
		DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();
		DiagramLinkage[] selectedLinkages = getProject().getOnlySelectedLinkages();
		int combinedLengths = selectedLinkages.length + selectedNodes.length;
		
		if (combinedLengths != 1)
			return false;
	    
		return (isNode(selectedNodes) || isLinkage(selectedLinkages));
	}

	private boolean isNode(DiagramNode[] selectedNodes)
	{
		boolean isNode = false;
		if (selectedNodes.length == 1)
			isNode = selectedNodes[0].isNode();
		return isNode;
	}

	private boolean isLinkage(DiagramLinkage[] selectedLinkages)
	{
		boolean isLinkage  = false;
		if (selectedLinkages.length == 1 )
			isLinkage = selectedLinkages[0].isLinkage();
		
		return isLinkage;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		DiagramNode startingNode = getStartingNode();
		try
		{
			DiagramModel model = getProject().getDiagramModel();
			ChainObject chainObject = new ChainObject();
			chainObject.buildNormalChain(model, startingNode.getUnderlyingObject());
			ConceptualModelNode[] chainNodes = chainObject.getNodes().toNodeArray();
			ConceptualModelLinkage[] linksInChain = chainObject.getLinkages();
			
			selectLinksInChain(model, linksInChain);
			selectNodesInChain(model, chainNodes);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

	private DiagramNode getStartingNode()
	{
		DiagramNode[] onlySelectedNodes = getProject().getOnlySelectedNodes();
		if (onlySelectedNodes.length == 1)
			return onlySelectedNodes[0];
			
		DiagramLinkage[] onlySelectedLinkages = getProject().getOnlySelectedLinkages();
		DiagramLinkage selectedLinkage = onlySelectedLinkages[0];
		return selectedLinkage.getToNode();
	}

	private void selectNodesInChain(DiagramModel model, ConceptualModelNode[] chainNodes) throws Exception
	{
		for(int i = 0; i < chainNodes.length; ++i)
		{
			// convert CMNode to DiagramNode
			DiagramNode nodeToSelect = model.getNodeById((ModelNodeId)chainNodes[i].getId());
			DiagramView view = (DiagramView)getView();
			view.getDiagramComponent().addSelectionCell(nodeToSelect);
		}
	}

	private void selectLinksInChain(DiagramModel model, ConceptualModelLinkage[] linksInChain) throws Exception
	{
		for (int i = 0 ; i < linksInChain.length; i++)
		{
			DiagramLinkage linkToSelect = model.getLinkageById((ModelLinkageId)linksInChain[i].getId());
			DiagramView view = (DiagramView)getView();
			view.getDiagramComponent().addSelectionCell(linkToSelect);
		}
	}

}
