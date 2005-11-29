/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.main.EAM;

public class InsertIndirectFactor extends InsertNode
{
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_INDIRECT_FACTOR;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Indirect Factor");
	}

}
