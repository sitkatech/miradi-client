/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.views;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;


public class MultilineNodeView extends VertexView
{
    public MultilineNodeView(DiagramFactor nodeToUse) 
    {
        super(nodeToUse);
        node = nodeToUse;
    }

	public Point2D getPerimeterPoint(EdgeView arg0, Point2D arg1, Point2D arg2)
	{
		EAM.logWarning("MultilineNodeView.getPerimeterPoint not implemented!");
		return super.getPerimeterPoint(arg0, arg1, arg2);
	}
	
	public Rectangle2D getRectangleWithoutAnnotations()
	{
		Rectangle2D rectangleCopy = (Rectangle2D)getBounds().clone();
		Dimension sizeWithoutAnnotations = MultilineNodeRenderer.getSizeWithoutAnnotations(rectangleCopy.getBounds().getSize(), node.getAnnotationRows());
		rectangleCopy.setRect(getBounds().getX(), getBounds().getY(), sizeWithoutAnnotations.getWidth(), sizeWithoutAnnotations.getHeight());
		return rectangleCopy;
	}

	protected DiagramFactor node;
}
