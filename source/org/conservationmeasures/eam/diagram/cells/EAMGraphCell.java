/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.jgraph.graph.DefaultGraphCell;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isProjectScope()
	{
		return false;
	}
	
	public boolean isFactorLink()
	{
		return false;
	}
	
	public DiagramFactorLink getDiagramFactorLink()
	{
		return null;
	}
}
