/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

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
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.NodeAnnotation;
import org.conservationmeasures.eam.diagram.nodes.NodeAnnotations;
import org.conservationmeasures.eam.diagram.nodes.ProjectVisionAnnotation;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;
import org.martus.util.xml.XmlUtilities;

public class MultilineCellRenderer extends JComponent implements CellViewRenderer
{
	public MultilineCellRenderer()
	{
		label = new JLabel();
	}

	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		EAMGraphCell cell = (EAMGraphCell)view.getCell();
		if(cell.isProjectScope())
		{
			isVisible = ((DiagramComponent)graphToUse).isProjectScopeVisible();
			vision = ((ProjectScopeBox)(view.getCell())).getVision();
		}

		String text = cell.toString();
		String formattedLabel = HTML_BEFORE_TEXT + XmlUtilities.getXmlEncoded(text) + HTML_AFTER_TEXT;
		label.setText(formattedLabel);
		graph = graphToUse;
		selected = sel;
		preview = previewMode;
		installAttributes(view.getAllAttributes());
		
		return this;
	}
	
	//Windows 2000 Quirk, this needs to be set or the graphic isn't filled in
	public static void setPaint(Graphics2D g2, Rectangle rect, Color color) 
	{
		g2.setPaint(new GradientPaint(rect.x, rect.y, color,
				rect.width, rect.height, color, false));
	}

	public void paint(Graphics g1)
	{
		if(!isVisible)
			return;
		Rectangle rect = getNonBorderBounds();

		Graphics2D g2 = (Graphics2D) g1;
		if (super.isOpaque())
		{
			g2.setColor(super.getBackground());
			if (gradientColor != null && !preview)
			{
				setOpaque(false);
				g2.setPaint(new GradientPaint(0, 0, getBackground(),
						getWidth(), getHeight(), gradientColor, true));
			}
			fillShape(g2, rect, getBackground());
		}
		
		int xInset = getInsetDimension().width;
		int yInset = getInsetDimension().height;
		label.setBorder(new EmptyBorder(yInset, xInset, yInset, xInset));
		label.setSize(getSize());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(getVerticalAlignmentOfText());
		label.paint(g2);
		
		if (bordercolor != null)
		{
			Color color = bordercolor;
			Stroke stroke = new BasicStroke(borderWidth);
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2, rect, Color.BLACK);
		}
		if (selected)
		{
			Color color = graph.getHighlightColor();
			Stroke stroke = GraphConstants.SELECTION_STROKE;
			g2.setColor(color);
			g2.setStroke(stroke);
			drawBorder(g2, rect, Color.BLACK);
		}

		drawProjectScopeVision(g2, rect);
	}
	
	private void drawProjectScopeVision(Graphics2D g2, Rectangle rect)
	{
		if(vision == null || vision.length() == 0)
			return;
		ProjectVisionAnnotation annotations = new ProjectVisionAnnotation();
		NodeAnnotation annotation = new NodeAnnotation(vision);
		annotations.add(annotation);
		Rectangle scopeRect = rect;
		scopeRect.setSize(scopeRect.width, scopeRect.height - borderWidth);
		drawAnnotation(scopeRect, g2, annotations);
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

	void drawAnnotation(Rectangle rect, Graphics2D g2, NodeAnnotations annotations) 
	{
		if(annotations != null && annotations.hasAnnotation())
		{
			Rectangle annotationsRectangle = getAnnotationsRect(rect, annotations.size());
			setPaint(g2, annotationsRectangle, annotations.getColor());
			RectangleRenderer annotationRenderer = new RectangleRenderer();
			annotationRenderer.fillShape(g2, annotationsRectangle, annotations.getColor());

			drawBoarder(g2, annotationsRectangle, annotationRenderer);
			
			//TODO allow multiple Objectives
			String labelMessage = annotations.getAnnotation(0).toString();
			drawLabel(g2, annotationsRectangle, labelMessage, annotationsRectangle.getSize());
		}
	}

	void drawBoarder(Graphics2D g2, Rectangle annotationsRectangle, MultilineNodeRenderer annotationRenderer) 
	{
		Color color = Color.BLACK;
		Stroke stroke = new BasicStroke(borderWidth);
		g2.setColor(color);
		g2.setStroke(stroke);
		annotationRenderer.drawBorder(g2, annotationsRectangle, color);
	}

	Rectangle getAnnotationsRect(Rectangle rect, int numberLines) 
	{
		Rectangle annotationsRectangle = new Rectangle();
		annotationsRectangle.x = rect.x + INDICATOR_WIDTH;
		int annotationsHeight = numberLines * ANNOTATIONS_HEIGHT;
		annotationsRectangle.y = rect.y + (rect.height - annotationsHeight);
		annotationsRectangle.width = rect.width - (2 * INDICATOR_WIDTH);
		annotationsRectangle.height = annotationsHeight;
		return annotationsRectangle;
	}

	Rectangle getNonBorderBounds()
	{
		return new Rectangle(borderWidth - 1, borderWidth - 1, 
				getSize().width - borderWidth, getSize().height - borderWidth);
	}

	protected void installAttributes(Map attributes)
	{
		setOpaque(GraphConstants.isOpaque(attributes));
		Color foreground = GraphConstants.getForeground(attributes);
		setForeground((foreground != null) ? foreground : graph.getForeground());
		Color background = GraphConstants.getBackground(attributes);
		setBackground((background != null) ? background : graph.getBackground());
		Font font = GraphConstants.getFont(attributes);
		label.setFont((font != null) ? font : graph.getFont());
		Border border = GraphConstants.getBorder(attributes);
		bordercolor = GraphConstants.getBorderColor(attributes);
		if (border != null)
			setBorder(border);
		else if (bordercolor != null)
		{
			borderWidth = Math.max(1, Math.round(GraphConstants
					.getLineWidth(attributes)));
			setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
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
	
	public void fillShape(Graphics g, Rectangle rect, Color color)
	{
		Graphics2D g2 = (Graphics2D)g;
		Paint oldPaint = g2.getPaint();
		setPaint(g2, rect, color);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g2.setPaint(oldPaint);
	}

	public void drawBorder(Graphics2D g2, Rectangle rect, Color color)
	{
		if(selected)
			g2.setStroke(new BasicStroke(3));
		else
			g2.setStroke(new BasicStroke());

		Color oldColor = g2.getColor();
		g2.setColor(color);
		g2.drawRect(rect.x, rect.y, rect.width, rect.height);
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
	
	public static final String HTML_AFTER_TEXT = "</font></div></html>";
	public static final String HTML_BEFORE_TEXT = "<html><div align='center'><font size='4'>";

	static final int INDICATOR_WIDTH = 20;
	static final int INDICATOR_HEIGHT = 20;
	static final int ANNOTATIONS_HEIGHT = 20;

	JGraph graph;
	JLabel label;
	int borderWidth;
	Color bordercolor;
	Color gradientColor;
	boolean selected;
	boolean preview;
	boolean isVisible;
	
	String vision;
	
}
