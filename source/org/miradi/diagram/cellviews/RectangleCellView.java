/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cellviews;

import java.awt.geom.Point2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;
import org.miradi.diagram.renderers.MultilineCellRenderer;
import org.miradi.diagram.renderers.RectangleRenderer;
import org.miradi.main.EAM;


public class RectangleCellView extends VertexView
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
    	EAM.logWarning("WARNING: RectangleNodeView.getEditor not implemented");
        return null;
    }
    
	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(EdgeView arg0, Point2D source, Point2D p)
	{
		return ((RectangleRenderer)getRenderer()).getPerimeterPoint(this, source, p);
	}
	
	protected static MultilineCellRenderer rectangleRenderer = new MultilineCellRenderer();
}
