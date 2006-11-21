/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class InsertIndirectFactor extends InsertNode
{
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_FACTOR;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Contributing Factor");
	}

	void linkToPreviouslySelectedNodes(ModelNodeId newlyInsertedId, DiagramNode[] nodesToLinkTo) throws CommandFailedException
	{
		super.linkToPreviouslySelectedNodes(newlyInsertedId, nodesToLinkTo);
		ConceptualModelNode insertedNode = getProject().findNode(newlyInsertedId);
		if(!insertedNode.isIndirectFactor())
			warnNotIndirectFactor();
	}

	void warnNotIndirectFactor()
	{
		EAM.notifyDialog(EAM.text("Text|This is a Direct Threat because it is linked to a Target"));
	}
	
	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setIndirectFactorsVisible(true);
		getProject().getLayerManager().setDirectThreatsVisible(true);
	}

}
