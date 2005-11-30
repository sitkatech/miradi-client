/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeTarget;

public class DiagramTarget extends DiagramNode
{
	public DiagramTarget()
	{
		super(new NodeTypeTarget());
	}

	public boolean isTarget()
	{
		return true;
	}
	
	
}
