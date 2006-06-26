/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.views;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.diagram.renderers.RoundRectangleRenderer;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;

public class ClusterView extends CompoundVertexView
{
	public ClusterView(DiagramCluster clusterToUse)
	{
		super(clusterToUse);
		cluster = clusterToUse;
	}
	
    public CellViewRenderer getRenderer() 
    {
        return clusterRenderer;
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
	
	public Rectangle2D getRectangleWithoutAnnotations()
	{
		Rectangle2D rectangleCopy = (Rectangle2D)getBounds().clone();
		Dimension sizeWithoutAnnotations = MultilineNodeRenderer.getSizeWithoutAnnotations(rectangleCopy.getBounds().getSize(), cluster.getAnnotationRows());
		rectangleCopy.setRect(getBounds().getX(), getBounds().getY(), sizeWithoutAnnotations.getWidth(), sizeWithoutAnnotations.getHeight());
		return rectangleCopy;
	}

	DiagramCluster cluster;
	protected static RectangleRenderer clusterRenderer = new RoundRectangleRenderer();
}
