/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;


public class FlexibleGraphCell extends DefaultGraphCell
{
	public FlexibleGraphCell(NodeType cellType)
	{
		type = cellType;
		add(new DefaultPort());
	}
	
	public boolean isGoal()
	{
		return(type.isGoal());
	}
	
	public boolean isThreat()
	{
		return(type.isThreat());
	}
	
	public boolean isIntervention()
	{
		return(type.isIntervention());
	}
	
//	public boolean isEllipse()
//	{
//		return (type.isEllipse());
//	}
//
//	public boolean isHexagon()
//	{
//		return (type.isHexagon());
//	}
//	
//	public boolean isTriangle()
//	{
//		return type.isTriangle();
//	}
	
	NodeType type;
}
