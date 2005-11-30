/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeIntervention;

public class DiagramIntervention extends DiagramNode
{
	public DiagramIntervention()
	{
		super(new NodeTypeIntervention());
	}

	public boolean isIntervention()
	{
		return true;
	}
	
}
