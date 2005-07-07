/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;


public class FlexibleCell extends DefaultGraphCell
{
	public FlexibleCell(CellType cellType)
	{
		type = cellType;
		add(new DefaultPort());
	}
	
	public boolean isEllipse()
	{
		return (type.isEllipse());
	}

	public boolean isHexagon()
	{
		return (type.isHexagon());
	}
	
	public boolean isThreat()
	{
		return(type.isThreat());
	}
	
	public boolean isTriangle()
	{
		return type.isTriangle();
	}
	
	public void setThreatColor(Color color)
	{
		threatColor = color;
	}
	
	public Color getThreatColor()
	{
		return threatColor;
	}

	CellType type;
	Color threatColor;
}
