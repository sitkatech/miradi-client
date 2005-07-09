/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;


public abstract class NodeType
{
	public abstract Color getColor();

	public boolean isGoal()
	{
		return false;
	}
	
	public boolean isThreat()
	{
		return false;
	}
	
	public boolean isIntervention()
	{
		return false;
	}
	
//	public boolean isEllipse()
//	{
//		return false;
//	}
//	
//	public boolean isHexagon()
//	{
//		return false;
//	}
//	
//	public boolean isTriangle()
//	{
//		return false;
//	}

}
