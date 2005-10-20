/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes.types;

import java.awt.Color;



public class NodeTypeIndirectFactor extends NodeType
{
	public boolean isIndirectFactor()
	{
		return true;
	}

	public Color getColor()
	{
		return Color.ORANGE;
	}
}
