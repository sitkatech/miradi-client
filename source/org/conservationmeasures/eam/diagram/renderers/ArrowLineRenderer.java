/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.martus.swing.Utilities;

public class ArrowLineRenderer extends EdgeRenderer
{

	public Component getRendererComponent(JGraph graphToUse, CellView cellView, 
					boolean sel, boolean hasFocus, boolean previewMode)
	{
		ArrowLineRenderer renderer = 
			(ArrowLineRenderer)super.getRendererComponent(graphToUse, cellView, sel, hasFocus, previewMode);
		
		if(sel || isAttachedToSelectedFactor())
		{
			renderer.lineWidth = 4;
		}

		stressText = getLinkCell().getFactorLink().getStressLabel();

		return renderer;
	}

	private LinkCell getLinkCell()
	{
		return (LinkCell)view.getCell();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if(isArrowBodyVisible())
			drawStress(g);
	}
	
	
	protected Shape createShape()
	{
		Shape shape = super.createShape();
		DiagramComponent diagram = getDiagram();
		if(diagram == null)
			return shape;
		
		if(isArrowBodyVisible())
			return shape;
		
		return createStubLineShape(shape);
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
		
		boolean isFromFactorSelected = diagram.isCellSelected(getLinkCell().getFrom());
		boolean isToFactorSelected = diagram.isCellSelected(getLinkCell().getTo());
		
		boolean onlyOneFactorSelected = (diagram.getSelectionCount() == 1);
		if(onlyOneFactorSelected)
			return (isFromFactorSelected || isToFactorSelected);

		return (isFromFactorSelected && isToFactorSelected);
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

	public Rectangle2D getPaintBounds(EdgeView viewToUse) 
	{
		Rectangle2D graphBounds = super.getPaintBounds(viewToUse);

		LinkCell thisCell = (LinkCell)viewToUse.getCell();
		FactorLink factorLink = thisCell.getFactorLink();
		String text = factorLink.getStressLabel();
		if (text == null || text.length()==0)
			return graphBounds;
		
		Rectangle2D union = calculateNewBoundsForStress(graphBounds, text);
		return union;
	}

	
	private Rectangle2D calculateNewBoundsForStress(Rectangle2D graphBounds, String text)
	{
		Rectangle textBounds = calcalateCenteredAndCushioned(graphBounds, text);
		Rectangle2D union = graphBounds.createUnion(textBounds);
		return union;
	}

	
	private Rectangle calcalateCenteredAndCushioned(Rectangle2D linkBounds, String text)
	{
		Graphics2D g2 = (Graphics2D)fontGraphics;
		
		Rectangle2D centerStressWithin = linkBounds;
		PointList points = getLinkCell().getDiagramFactorLink().getBendPoints();
		if(points.size() > 0)
		{
			int centerPointIndex = points.size() / 2;
			Point point = points.get(centerPointIndex);
			centerStressWithin = new Rectangle(point, new Dimension(1,1));
		}

		TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
		Rectangle textBounds = textLayout.getBounds().getBounds();
		
		textBounds.width = textBounds.width + 2*CUSHION;
		textBounds.height = textBounds.height +2*CUSHION;
		
		Point upperLeftToDrawText = Utilities.center(textBounds.getSize(), centerStressWithin.getBounds().getBounds());
		textBounds.setLocation(upperLeftToDrawText);
		return textBounds;
	}


	

	private void drawStress(Graphics g)
	{
		if (!getLinkCell().getFactorLink().isTargetLink())
			return;
		
		if(stressText == null || stressText.length() < 1)
			return;
		
		
		Rectangle rectangle = calcalateCenteredAndCushioned(getPaintBounds(view), stressText);

		int arc = 5;

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(DiagramConstants.COLOR_STRESS);
		g2.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.drawString(stressText, rectangle.x+CUSHION, rectangle.y + rectangle.height-CUSHION);
	}
	
	private static final int CUSHION = 5;
	public static final int ARROW_STUB_LINE = 23253;
	
	boolean isVisible;
	String stressText;
}
