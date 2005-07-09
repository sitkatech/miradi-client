/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

public class NodeTypeGoal extends NodeType
{
	public boolean isGoal()
	{
		return true;
	}

	public Color getColor()
	{
		return Color.GREEN;
	}

}
