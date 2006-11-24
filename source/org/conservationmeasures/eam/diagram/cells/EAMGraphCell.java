/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.jgraph.graph.DefaultGraphCell;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isNode()
	{
		return false;
	}
	
	public boolean isProjectScope()
	{
		return false;
	}
	
	public boolean isLinkage()
	{
		return false;
	}
	
}
