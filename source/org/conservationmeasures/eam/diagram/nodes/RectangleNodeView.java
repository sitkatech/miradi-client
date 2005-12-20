/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.geom.Point2D;

import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleWithPriorityRenderer;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;


public class RectangleNodeView extends MultilineNodeView
{
	public RectangleNodeView(DiagramNode node)
	{
		super(node);
	}

    public CellViewRenderer getRenderer() 
    {
        return rectangleRenderer;
    }

    public GraphCellEditor getEditor() 
    {
    	EAM.logDebug("WARNING: RectangleNodeView.getEditor not implemented");
        return null;
    }
    
	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(EdgeView arg0, Point2D source, Point2D p)
	{
		return ((RectangleRenderer)getRenderer()).getPerimeterPoint(p, getRectangleWithoutAnnotations());
	}
	
	protected static RectangleRenderer rectangleRenderer = new RectangleWithPriorityRenderer();
}
