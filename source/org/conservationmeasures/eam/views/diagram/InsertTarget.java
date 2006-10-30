/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramTarget;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.main.EAM;

public class InsertTarget extends InsertNode
{
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_TARGET;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Target");
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramTarget.class, true);
	}
}
