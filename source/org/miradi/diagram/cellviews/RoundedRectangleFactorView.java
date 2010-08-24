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

import java.awt.geom.Point2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.renderers.RoundRectangleRenderer;
import org.miradi.main.EAM;

public class RoundedRectangleFactorView extends FactorView
{
	public RoundedRectangleFactorView(FactorCell factor)
	{
		super(factor);
	}

    public CellViewRenderer getRenderer() 
    {
        return roundedRectangleRenderer;
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
		return ((RoundRectangleRenderer)getRenderer()).getPerimeterPoint(p, getBounds());
	}
	
	private final static int DEFAULT_ARC_SIZE = 20;
	protected static RoundRectangleRenderer roundedRectangleRenderer = new RoundRectangleRenderer(DEFAULT_ARC_SIZE);

}
