/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes.types;

import java.awt.Color;

public class NodeTypeIntervention extends NodeType
{
	public boolean isIntervention()
	{
		return true;
	}
	
	public Color getColor()
	{
		return Color.YELLOW;
	}

}
