/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram.cellviews;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.main.EAM;


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
