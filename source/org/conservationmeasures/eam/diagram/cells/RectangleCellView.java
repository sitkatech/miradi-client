/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphCellEditor;


public class RectangleCellView extends MultilineCellView
{
	public RectangleCellView(Object cell)
	{
		super(cell);
	}

    public CellViewRenderer getRenderer() 
    {
        return rectangleRenderer;
    }

    public GraphCellEditor getEditor() 
    {
        return null;
    }
    
	protected static RectangleRenderer rectangleRenderer = new RectangleRenderer();
}
