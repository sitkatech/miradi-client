/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodes.types.NodeType;

public class DiagramFactor extends DiagramNode
{
	public DiagramFactor(NodeType nodeType)
	{
		super(nodeType);
	}

	public boolean isIndirectFactor()
	{
		return(getType().isIndirectFactor());
	}
	
	public boolean isDirectThreat()
	{
		return(getType().isDirectThreat());
	}
	
	public boolean isStress()
	{
		return(getType().isStress());
	}
	
}
