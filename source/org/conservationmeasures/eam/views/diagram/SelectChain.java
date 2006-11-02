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
		if(selectedNodes.length != 1)
			return false;
	
		return (selectedNodes[0].isNode());
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			DiagramNode selectedNode = getProject().getOnlySelectedNodes()[0];
			
			DiagramModel model = getProject().getDiagramModel();
			ChainObject chainObject = new ChainObject();
			chainObject.buildNormalChain(model, selectedNode.getUnderlyingObject());
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
