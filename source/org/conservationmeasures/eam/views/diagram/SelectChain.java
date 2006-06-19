/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectChain extends ViewDoer
{
	public boolean isAvailable()
	{
		DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();
		if(selectedNodes.length != 1)
			return false;
		
		return (selectedNodes[0].isDirectThreat());
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			DiagramNode selectedNode = getProject().getOnlySelectedNodes()[0];
			ConceptualModelNode[] chainNodes = getProject().getDiagramModel().getDirectThreatChainNodes(selectedNode.getUnderlyingObject()).toNodeArray();
			for(int i = 0; i < chainNodes.length; ++i)
			{
				DiagramView view = (DiagramView)getView();
				view.getDiagramComponent().addSelectionCell(chainNodes[i]);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error");
		}
	}

}
