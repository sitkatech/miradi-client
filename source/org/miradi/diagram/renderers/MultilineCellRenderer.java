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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;
import org.martus.util.xml.XmlUtilities;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.main.MainWindow;

public class MultilineCellRenderer extends JComponent implements CellViewRenderer
{
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		if (htmlFormViewer==null)
		{
			MainWindow mainWindow = ((DiagramComponent)graphToUse).getMainWindow();
			htmlFormViewer = new FactorHtmlViewer(mainWindow);
			htmlFormViewer.setOpaque(false);
		}
		EAMGraphCell cell = (EAMGraphCell)view.getCell();
		String text = cell.toString();
		
		String formattedLabel =  XmlUtilities.getXmlEncoded(text);
		htmlFormViewer.setFactorCell(cell);
		setHtmlFormViewerText(formattedLabel);
		
		graph = graphToUse;
		selected = sel;
		preview = previewMode;
		installAttributes(view.getAllAttributes());
		return this;
	}

	protected void setHtmlFormViewerText(String formattedLabel)
	{
		htmlFormViewer.setText(formattedLabel);
	}
	
	//Windows 2000 Quirk, this needs to be set or the graphic isn't filled in
	public static void setPaint(Graphics2D g2, Rectangle rect, Color color) 
	{
		g2.setPaint(new GradientPaint(rect.x, rect.y, color,
				rect.width, rect.height, color, false));
	}

	public void paint(Graphics g1)
	{
		Rectangle rect = getNonBorderBounds();

		Graphics2D g2 = (Graphics2D) g1;
		if (super.isOpaque())
		{
			g2.setColor(getFillColor());
			if (gradientColor != null && !preview)
			{
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, getFillColor(),
						getWidth(), getHeight(), gradientColor, true));
			}
			fillShape(g2, rect, getFillColor());
		}
		
				
		Rectangle textAreaRect = getMainTextRect();
		htmlFormViewer.setSize(textAreaRect.getSize());
		htmlFormViewer.setMaximumSize(textAreaRect.getSize());
		
		Shape clip = g2.getClip();
		g2.clip(getShape(rect));
		g2.translate(textAreaRect.x, textAreaRect.y);
		htmlFormViewer.paint(g2);
		g2.translate(-textAreaRect.x, -textAreaRect.y);
		g2.setClip(clip);
		
		
		if (bordercolor != null)
		{
			Color color = bordercolor;
			Stroke stroke = getStroke();
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2, rect, Color.BLACK);
		}
		
		graph.setHandleSize(HANDLE_SIZE);
		
		if (selected)
		{
			Color color = graph.getHighlightColor();
			Stroke stroke = getSelectionStroke();
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2, rect, Color.BLACK);
		}
	}

	protected Rectangle getMainTextRect()
	{
		Rectangle textAreaRect = getNonBorderBounds();
		int xInset = getInsetDimension().width;
		int yInset = getInsetDimension().height;
	
		textAreaRect.x += xInset;
		textAreaRect.y += yInset;
		textAreaRect.height -= (2 * yInset);
		textAreaRect.width -= (2 * xInset);
		
		//NOTE: To make sure that text does not overflow horizontally, the below padding has been 
		//added.  the amount is arbitrary and was derived from trial and error. The padding amount is
		//still not perfect, you can see this with different text and zoom scales.
		int FACTOR_TEXT_HORIZONTAL_PADDING = -5;
		textAreaRect.grow(FACTOR_TEXT_HORIZONTAL_PADDING, 0);
		
		return textAreaRect;
	}

	Stroke getStroke()
	{
		return  new BasicStroke(borderThickness);
	}
	
	Stroke getSelectionStroke()
	{
		float[] dash = { 20f, 5f };

		Stroke SELECTION_STROKE = new BasicStroke(getSelectedStrokeWidth(),
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		
		return SELECTION_STROKE;
	}

	protected int getSelectedStrokeWidth()
	{
		return borderThickness + 4;
	}

	Color getFillColor()
	{
		return super.getBackground();
	}
	
	protected void drawLabel(Graphics2D g2, Rectangle labelRectangle, String labelMessage, Dimension size) 
	{
		JLabel message = new JLabel(labelMessage);
		message.setSize(size);
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.CENTER);

		// The graphics2D object controls the location where the label 
		// will paint (the label's location will be ignored at this point)
		// Tell g2 where the new origin is, paint, and revert to the original origin
		g2.translate(labelRectangle.x, labelRectangle.y);
		message.paint(g2);
		g2.translate(-labelRectangle.x, -labelRectangle.y);
	}

	void drawAnnotationCellRect(Rectangle rect, Graphics2D g2, FactorRenderer annotationRenderer, String annotationText) 
	{
		if(annotationText == null)
			return;

		Rectangle annotationsRectangle = getAnnotationsRectFromCellRect(rect);
		setPaint(g2, annotationsRectangle, ANNOTATIONS_COLOR);
		drawAnnotation(annotationsRectangle, g2, annotationRenderer, annotationText);
	}

	void drawAnnotation(Rectangle annotationsRectangle, Graphics2D g2, FactorRenderer annotationRenderer, String annotationText)
	{
		annotationRenderer.fillShape(g2, annotationsRectangle, ANNOTATIONS_COLOR);
		drawAnnotationBorder(g2, annotationsRectangle, annotationRenderer);
		
		//TODO allow multiple Objectives
		String labelMessage = annotationText;
		drawLabel(g2, annotationsRectangle, labelMessage, annotationsRectangle.getSize());
	}

	void drawAnnotationBorder(Graphics2D g2, Rectangle annotationsRectangle, FactorRenderer annotationRenderer) 
	{
		Color color = Color.BLACK;
		Stroke stroke = new BasicStroke(borderThickness);
		g2.setColor(color);
		g2.setStroke(stroke);
		annotationRenderer.drawBorder(g2, annotationsRectangle, color);
	}

	Rectangle getAnnotationsRectFromCellRect(Rectangle rect) 
	{
		Rectangle annotationsRectangle = new Rectangle();
		annotationsRectangle.x = getAnnotationX(rect.x);
		annotationsRectangle.y = getAnnotationY(rect.y, rect.height);
		annotationsRectangle.width = getAnnotationsWidth(rect.width);
		annotationsRectangle.height = getAnnotationsHeight();
		return annotationsRectangle;
	}
	
	int getAnnotationX(int cellX)
	{
		return cellX + getAnnotationLeftOffset();
	}
	
	int getAnnotationY(int cellY, int cellHeight)
	{
		return cellY + cellHeight - getAnnotationsHeight();
	}

	int getAnnotationsWidth(int cellWidth)
	{
		return cellWidth - getAnnotationLeftOffset() - getAnnotationRightInset();
	}

	int getAnnotationsHeight()
	{
		return ANNOTATIONS_HEIGHT;
	}

	int getAnnotationLeftOffset()
	{
		return INDICATOR_WIDTH;
	}
	
	int getAnnotationRightInset()
	{
		return INDICATOR_WIDTH;
	}

	Rectangle getNonBorderBounds()
	{
		return new Rectangle(borderThickness - 1, borderThickness - 1, 
				getSize().width - borderThickness, getSize().height - borderThickness);
	}

	protected void installAttributes(Map attributes)
	{
		setOpaque(GraphConstants.isOpaque(attributes));
		Color foreground = GraphConstants.getForeground(attributes);
		setForeground((foreground != null) ? foreground : graph.getForeground());
		Color background = GraphConstants.getBackground(attributes);
		setBackground((background != null) ? background : graph.getBackground());
		Font font = GraphConstants.getFont(attributes);
		htmlFormViewer.setFont((font != null) ? font : graph.getFont());
		Border border = GraphConstants.getBorder(attributes);
		bordercolor = GraphConstants.getBorderColor(attributes);
		if (border != null)
			setBorder(border);
		else if (bordercolor != null)
		{
			borderThickness = Math.max(1, Math.round(GraphConstants
					.getLineWidth(attributes)));
			setBorder(BorderFactory.createLineBorder(bordercolor, borderThickness));
		}
		gradientColor = GraphConstants.getGradientColor(attributes);
	
	}

	int getVerticalAlignmentOfText()
	{
		return JLabel.TOP;
	}
	
	Dimension getInsetDimension()
	{
		return new Dimension(0, 0);
	}
	
	public Shape getShape(Rectangle rect)
	{
		return new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, CORNER_SIZE, CORNER_SIZE);
	}
	
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g2.fill(getShape(rect));
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		Color oldColor = g2.getColor();
		g2.setColor(color);
		g2.draw(getShape(rect));
		g2.setColor(oldColor);
	}
	
	/*
	 * This method has a different copyright than the rest of this file,
	 * because it was copied directly from jgraph's VertexRenderer class:
	 * 
	 * Copyright (c) 2001-2004 Gaudenz Alder
	 *   
 	 */
	/**
	 * Returns the intersection of the bounding rectangle and the straight line
	 * between the source and the specified point p. The specified point is
	 * expected not to intersect the bounds.
	 */
	public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p) {
		Rectangle2D bounds = view.getBounds();
		double x = bounds.getX();
		double y = bounds.getY();
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		double xCenter = x + width / 2;
		double yCenter = y + height / 2;
		double dx = p.getX() - xCenter; // Compute Angle
		double dy = p.getY() - yCenter;
		double alpha = Math.atan2(dy, dx);
		double xout = 0, yout = 0;
		double pi = Math.PI;
		double pi2 = Math.PI / 2.0;
		double beta = pi2 - alpha;
		double t = Math.atan2(height, width);
		if (alpha < -pi + t || alpha > pi - t) { // Left edge
			xout = x;
			yout = yCenter - width * Math.tan(alpha) / 2;
		} else if (alpha < -t) { // Top Edge
			yout = y;
			xout = xCenter - height * Math.tan(beta) / 2;
		} else if (alpha < t) { // Right Edge
			xout = x + width;
			yout = yCenter + width * Math.tan(alpha) / 2;
		} else { // Bottom Edge
			yout = y + height;
			xout = xCenter + height * Math.tan(beta) / 2;
		}
		return new Point2D.Double(xout, yout);
	}
	
	public static final int INDICATOR_WIDTH = 20;
	public static final int INDICATOR_HEIGHT = 20;
	public static final int ANNOTATIONS_HEIGHT = 20;
	protected static final int CORNER_SIZE = 20;
	private static final int HANDLE_SIZE = 4;

	private static final Color LIGHT_BLUE = new Color(204,238,255);
	public static final Color ANNOTATIONS_COLOR = LIGHT_BLUE;

	JGraph graph;
	FactorHtmlViewer htmlFormViewer;
	int borderThickness;
	Color bordercolor;
	Color gradientColor;
	boolean selected;
	boolean preview;
}
	