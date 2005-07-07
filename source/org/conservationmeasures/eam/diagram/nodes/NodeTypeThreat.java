/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;



public class NodeTypeThreat extends NodeType
{
	public boolean isThreat()
	{
		return true;
	}

	public Color getColor()
	{
		return new Color(0, 255, 0);
	}
}
