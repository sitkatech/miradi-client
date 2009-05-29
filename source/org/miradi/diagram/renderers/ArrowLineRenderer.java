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
package org.miradi.diagram.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ArrowLineRenderer extends EdgeRenderer
{

	public Component getRendererComponent(JGraph graphToUse, CellView cellView, boolean sel, boolean hasFocus, boolean previewMode)
	{
		ArrowLineRenderer renderer = (ArrowLineRenderer)super.getRendererComponent(graphToUse, cellView, sel, hasFocus, previewMode);
		diagramLinkColor = getLinkCell().getColor();
		linkSelected = sel;
		if(sel || isAttachedToSelectedFactor())
		{
			renderer.lineWidth = 4;
		}

		return renderer;
	}
	
	private LinkCell getLinkCell()
	{
		return (LinkCell)view.getCell();
	}
	
	@Override
	public Rectangle2D getPaintBounds(EdgeView viewToUse)
	{
		//NOTE: This does not care about the actual shape but just the bounds.
		Shape shape = createShapeWithRoundedCorners();
		Rectangle2D shapeBounds = shape.getBounds2D();
		clearViewShape();
		
		return shapeBounds;	
	}

	private void clearViewShape()
	{
		view.sharedPath = null;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if (linkSelected)
		{
			g2.setColor(diagramLinkColor);
			Stroke stroke = getSelectionStroke();
			g2.setStroke(stroke);
			if (view.lineShape != null)
				g2.draw(view.lineShape);
		}
	}
	
	Stroke getSelectionStroke()
	{
		float[] dash = { 20f, 5f };
		return new BasicStroke(6, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
	}

	
	
	protected Shape createShape()
	{
		Shape shape = createShapeWithRoundedCorners();
		if(isArrowBodyVisible())
			return shape;
		
		return createStubLineShape(shape);
	}

	/**
	 * NOTE This code was copied directly from jgraph source, 
	 * and has enough side effects and interactions that any version 
	 * upgrades will probably break it.
	 */
	protected Shape createShapeWithRoundedCorners() {
		int n = view.getPointCount();
		if (n > 1) {
			// Following block may modify static vars as side effect (Flyweight
			// Design)
			EdgeView tmp = view;
			Point2D[] p = null;
			p = new Point2D[n];
			for (int i = 0; i < n; i++) {
				Point2D pt = tmp.getPoint(i);
				if (pt == null)
					return null; // exit
				p[i] = new Point2D.Double(pt.getX(), pt.getY());
			}

			// End of Side-Effect Block
			// Undo Possible MT-Side Effects
			if (view != tmp) {
				view = tmp;
				installAttributes(view);
			}
			// End of Undo
			if (view.sharedPath == null) {
				view.sharedPath = new GeneralPath(GeneralPath.WIND_NON_ZERO, n);
			} else {
				view.sharedPath.reset();
			}
			view.beginShape = view.lineShape = view.endShape = null;
			Point2D p0 = p[0];
			Point2D pe = p[n - 1];
			Point2D p1 = p[1];
			Point2D p2 = p[n - 2];

// The following code is not needed becuase we ALWAYS want our curved-corner lines
//			if (lineStyle == GraphConstants.STYLE_BEZIER && n > 2) {
//				bezier = new Bezier(p);
//				p2 = bezier.getPoint(bezier.getPointCount() - 1);
//			} else if (lineStyle == GraphConstants.STYLE_SPLINE && n > 2) {
//				spline = new Spline2D(p);
//				double[] point = spline.getPoint(0.9875);
//				// Extrapolate p2 away from the end point, pe, to avoid integer
//				// rounding errors becoming too large when creating the line end
//				double scaledX = pe.getX() - ((pe.getX() - point[0]) * 128);
//				double scaledY = pe.getY() - ((pe.getY() - point[1]) * 128);
//				p2.setLocation(scaledX, scaledY);
//			}

			if (beginDeco != GraphConstants.ARROW_NONE) {
				view.beginShape = createLineEnd(beginSize, beginDeco, p1, p0);
			}
			if (endDeco != GraphConstants.ARROW_NONE) {
				view.endShape = createLineEnd(endSize, endDeco, p2, pe);
			}
			view.sharedPath.moveTo((float) p0.getX(), (float) p0.getY());
//	The following code is not needed becuase we ALWAYS want our curved-corner lines
//			/* THIS CODE WAS ADDED BY MARTIN KRUEGER 10/20/2003 */
//			if (lineStyle == GraphConstants.STYLE_BEZIER && n > 2) {
//				Point2D[] b = bezier.getPoints();
//				view.sharedPath.quadTo((float) b[0].getX(),
//						(float) b[0].getY(), (float) p1.getX(), (float) p1
//								.getY());
//				for (int i = 2; i < n - 1; i++) {
//					Point2D b0 = b[2 * i - 3];
//					Point2D b1 = b[2 * i - 2];
//					view.sharedPath.curveTo((float) b0.getX(), (float) b0
//							.getY(), (float) b1.getX(), (float) b1.getY(),
//							(float) p[i].getX(), (float) p[i].getY());
//				}
//				view.sharedPath.quadTo((float) b[b.length - 1].getX(),
//						(float) b[b.length - 1].getY(),
//						(float) p[n - 1].getX(), (float) p[n - 1].getY());
//			} else if (lineStyle == GraphConstants.STYLE_SPLINE && n > 2) {
//				for (double t = 0; t <= 1; t += 0.0125) {
//					double[] xy = spline.getPoint(t);
//					view.sharedPath.lineTo((float) xy[0], (float) xy[1]);
//				}
//			}
//			/* END */
//			else {
//				for (int i = 1; i < n - 1; i++)
//					view.sharedPath.lineTo((float) p[i].getX(), (float) p[i]
//							.getY());
//				view.sharedPath.lineTo((float) pe.getX(), (float) pe.getY());
//			}
			{
				for (int i = 1; i < n-1; i++)
				{
					Point2D beforePoint = p[i-1];
					Point2D apexPoint = p[i];
					Point2D afterPoint = p[i+1];
					
					CurvePoint startCurvePoint = new CurvePoint(beforePoint, apexPoint);
					CurvePoint endCurvePoint = new CurvePoint(afterPoint, apexPoint);
					
					view.sharedPath.lineTo((float) startCurvePoint.start.getX(), 
							(float) startCurvePoint.start.getY());
					view.sharedPath.quadTo((float) endCurvePoint.apex.getX(), 
							(float) endCurvePoint.apex.getY(),
							(float) endCurvePoint.start.getX(),
							(float) endCurvePoint.start.getY()							);
				}

				view.sharedPath.lineTo((float) pe.getX(), (float) pe.getY());
			}
			view.sharedPath.moveTo((float) pe.getX(), (float) pe.getY());
			if (view.endShape == null && view.beginShape == null) {
				// With no end decorations the line shape is the same as the
				// shared path and memory
				view.lineShape = view.sharedPath;
			} else {
				view.lineShape = (GeneralPath) view.sharedPath.clone();
				if (view.endShape != null)
					view.sharedPath.append(view.endShape, true);
				if (view.beginShape != null)
					view.sharedPath.append(view.beginShape, true);
			}
			return view.sharedPath;
		}
		return null;
	}
	
	class CurvePoint
	{
		CurvePoint(Point2D segmentStart, Point2D apexAt)
		{
			apex = apexAt;

			double segmentLength = apexAt.distance(segmentStart);
			double curveLength = Math.min(Project.DEFAULT_GRID_SIZE, segmentLength/2);
			double curveRatio = curveLength / segmentLength;
			
			double startX = computeStart(apex.getX(), segmentStart.getX(), curveRatio);
			double startY = computeStart(apex.getY(), segmentStart.getY(), curveRatio);
			start = new Point2D.Double(startX, startY);
		}
		
		double computeStart(double segmentStart, double apexAt, double ratio)
		{
			return segmentStart + (apexAt - segmentStart) * ratio;
		}
		
		Point2D apex;
		Point2D start;
	}

	private Shape createStubLineShape(Shape shape)
	{
		GeneralPath pathShape = (GeneralPath)shape;
		pathShape.reset();
		if(view.beginShape != null)
			pathShape.append(view.beginShape, false);
		if(view.endShape != null)
			pathShape.append(view.endShape, false);
		view.lineShape = null;
		return pathShape;
	}

	private boolean isArrowBodyVisible()
	{
		return getLinkCell().isThisLinkBodyVisible(getDiagram());
	}
	
	private DiagramComponent getDiagram()
	{
		if(graph == null)
			return null;
		DiagramComponent diagram = (DiagramComponent)graph.get();
		return diagram;
	}

	private boolean isAttachedToSelectedFactor()
	{
		DiagramComponent diagram = getDiagram();
		if(diagram == null)
			return false;
		
		try
		{
			return getLinkCell().isLinkedToAnySelectedFactor(diagram);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}		
	}

	protected Shape createLineEnd(int size, int style, Point2D src, Point2D dst)
	{
		DiagramComponent diagram = getDiagram();
		if(style != ARROW_STUB_LINE || src == null || dst == null || diagram == null)
			return super.createLineEnd(size, style, src, dst);
		
		return createStubLine(size, src, dst);
	}

	private Shape createStubLine(int size, Point2D src, Point2D dst)
	{
		int d = (int) Math.max(1, dst.distance(src));
		int ax = (int) -(size * (dst.getX() - src.getX()) / d);
		int ay = (int) -(size * (dst.getY() - src.getY()) / d);
		Polygon poly = new Polygon();
		poly.addPoint((int) dst.getX(), (int) dst.getY());
		dst.setLocation(dst.getX() + ax, dst.getY() + ay);
		poly.addPoint((int) dst.getX(), (int) dst.getY());
		return poly;
	}

	public static final int ARROW_STUB_LINE = 23253;

	private boolean linkSelected;
	private Color diagramLinkColor;
}
