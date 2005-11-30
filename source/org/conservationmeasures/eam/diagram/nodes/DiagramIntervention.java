/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

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
	
	public boolean canHaveObjectives()
	{
		return true;
	}

	public Color getColor()
	{
		return COLOR_INTERVENTION;
	}

	public static final Color COLOR_INTERVENTION = Color.YELLOW;

}
