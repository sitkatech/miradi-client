/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes.types;

import java.awt.Color;

public class NodeTypeStress extends NodeType {

	public Color getColor() 
	{
		return Color.MAGENTA;
	}

	public boolean isStress() 
	{
		return true;
	}
}
