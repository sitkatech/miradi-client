/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

public class DiagramTarget extends DiagramNode
{
	public DiagramTarget(ConceptualModelTarget cmTarget)
	{
		super(cmTarget);
	}

	public boolean isTarget()
	{
		return true;
	}
	
	public boolean canHaveGoal()
	{
		return true;
	}

	public Color getColor()
	{
		return COLOR_TARGET;
	}

	public static final Color COLOR_TARGET = Color.GREEN;
	
}
