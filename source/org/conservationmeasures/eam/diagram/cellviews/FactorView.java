/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;


public class FactorView extends VertexView
{
    public FactorView(FactorCell factorToUse) 
    {
        super(factorToUse);
        factor = factorToUse;
    }

	public Point2D getPerimeterPoint(EdgeView arg0, Point2D arg1, Point2D arg2)
	{
		EAM.logWarning("MultilineNodeView.getPerimeterPoint not implemented!");
		return super.getPerimeterPoint(arg0, arg1, arg2);
	}
	
	public Rectangle2D getRectangleWithoutAnnotations()
	{
		Rectangle2D rectangleCopy = (Rectangle2D)getBounds().clone();
		Dimension sizeWithoutAnnotations = FactorRenderer.getSizeWithoutAnnotations(rectangleCopy.getBounds().getSize());
		rectangleCopy.setRect(getBounds().getX(), getBounds().getY(), sizeWithoutAnnotations.getWidth(), sizeWithoutAnnotations.getHeight());
		return rectangleCopy;
	}

	protected FactorCell factor;
}
