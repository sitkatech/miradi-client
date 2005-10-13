/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphSelectionModel;

public class SelectionModelWithLayers extends DefaultGraphSelectionModel
{
	public SelectionModelWithLayers(JGraph diagramToUse)
	{
		super(diagramToUse);
	}
	
	private boolean isSelectable(Object cellObject)
	{
		EAMGraphCell cell = (EAMGraphCell)cellObject;
		DiagramComponent diagram = (DiagramComponent)graph;
		boolean isVisible = false;
		if(cell.isLinkage())
			isVisible = diagram.isLinkageVisible((DiagramLinkage)cell);
		else if(cell.isNode())
			isVisible = diagram.isNodeVisible((DiagramNode)cell);
		return isVisible;
	}

	public void addSelectionCell(Object cell)
	{
		if(isSelectable(cell))
			super.addSelectionCell(cell);
	}

	public void addSelectionCells(Object[] cells)
	{
		Vector selectableCells = new Vector();
		for(int i=0; i < cells.length; ++i)
		{
			if(isSelectable(cells[i]))
				selectableCells.add(cells[i]);
		}
		super.addSelectionCells(selectableCells.toArray());
	}

	public void setSelectionCell(Object cell)
	{
		if(isSelectable(cell))
			super.setSelectionCell(cell);
	}

	public void setSelectionCells(Object[] cells)
	{
		Vector selectableCells = new Vector();
		for(int i=0; i < cells.length; ++i)
		{
			if(isSelectable(cells[i]))
				selectableCells.add(cells[i]);
		}
		super.setSelectionCells(selectableCells.toArray());
	}
}