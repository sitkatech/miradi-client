/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

public class NodeTypeDirectThreat extends NodeType {

	public Color getColor() 
	{
		return Color.PINK;
	}

	public boolean isDirectThreat() 
	{
		return true;
	}
}
