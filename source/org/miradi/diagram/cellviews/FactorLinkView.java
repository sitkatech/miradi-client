/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.jdesktop.swingx.util.OS;
import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.miradi.diagram.renderers.ArrowLineRenderer;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class FactorLinkView extends EdgeView
{
	public FactorLinkView(Object edge)
	{
		super(edge);
	}

	@Override
	public CellViewRenderer getRenderer()
	{
		return arrowLineRenderer;
	}
	
	@Override
	public CellHandle getHandle(GraphContext context)
	{
		return new EdgeHandleWithBendPointSelection(this, context);
	}

	@Override
	public boolean intersects(JGraph graph, Rectangle2D rect)
	{
		boolean intersects = super.intersects(graph, rect);
		if (!intersects)
			intersects = checkForBendPointsIntersect(graph, rect);

		return intersects;
	}

	private boolean checkForBendPointsIntersect(JGraph graph, Rectangle2D rect)
	{
		boolean intersects = false;

		// MRD-5961 - work-around for not being able to select links with bend points on OS X
		Graphics2D g2 = (Graphics2D) graph.getGraphics();
		if (g2 != null && OS.isMacOSX() && this.getPointCount() > 2)
		{
			PathIterator pi = this.getShape().getPathIterator(null);
			Point2D.Double start = null;
			Point2D.Double point1 = null;
			Point2D.Double point2 = null;

			while (!pi.isDone())
			{
				double[] coordinates = new double[6];

				switch (pi.currentSegment(coordinates))
				{
					case PathIterator.SEG_MOVETO:
						point2 = new Point2D.Double(coordinates[0], coordinates[1]);
						point1 = null;
						start = (Point2D.Double) point2.clone();
						break;
					case PathIterator.SEG_LINETO:
						point1 = point2;
						point2 = new Point2D.Double(coordinates[0], coordinates[1]);
						break;
					case PathIterator.SEG_CLOSE:
						point1 = point2;
						point2 = start;
						break;
				}

				if (point1 != null)
				{
					Line2D segment = new Line2D.Double(point1, point2);
					if (segment.intersects(rect))
					{
						intersects = true;
						break;
					}
				}

				pi.next();
			}
		}

		return intersects;
	}

	private static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
