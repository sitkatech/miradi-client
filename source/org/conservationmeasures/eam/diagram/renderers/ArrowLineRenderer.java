/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
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
		
		linkage = (DiagramFactorLink)cellView.getCell();
		if(sel)
		{
			renderer.lineWidth = 4;
		}

		DiagramComponent diagram = (DiagramComponent)graphToUse;
		isVisible = diagram.areLinkagesVisible();

		return renderer;
	}

	public void paint(Graphics g)
	{
		if(!isVisible)
			return;
		
		super.paint(g);
		drawStress(g);
		
	}
	
	public Rectangle2D getPaintBounds(EdgeView viewToUse) 
	{
		Rectangle2D graphBounds = super.getPaintBounds(viewToUse);
		DiagramFactorLink linkageToUse = (DiagramFactorLink)viewToUse.getCell();
		
		String text = linkageToUse.getStressLabel();
		if (text.length()==0)
			return graphBounds;
		
		Rectangle2D union = calculateNewBoundsForStress(graphBounds, linkageToUse);
		return union;
	}

	
	private Rectangle2D calculateNewBoundsForStress(Rectangle2D graphBounds, DiagramFactorLink linkageToUse)
	{
		Rectangle textBounds = calcalateCenteredAndCushioned(graphBounds, linkageToUse);
		Rectangle2D union = graphBounds.createUnion(textBounds);
		return union;
	}

	
	private Rectangle calcalateCenteredAndCushioned(Rectangle2D graphBounds, DiagramFactorLink linkageToUse)
	{
		Graphics2D g2 = (Graphics2D)fontGraphics;
		
		TextLayout textLayout = new TextLayout(linkageToUse.getStressLabel(), g2.getFont(), g2.getFontRenderContext());
		Rectangle textBounds = textLayout.getBounds().getBounds();
		
		textBounds.width = textBounds.width + 2*CUSHION;
		textBounds.height = textBounds.height +2*CUSHION;
		
		Point upperLeftToDrawText = Utilities.center(textBounds.getSize(), graphBounds.getBounds().getBounds());
		textBounds.setLocation(upperLeftToDrawText);
		return textBounds;
	}


	

	private void drawStress(Graphics g)
	{
		if(!linkage.getToNode().isTarget())
			return;
		
		String text = linkage.getStressLabel();
		if(text == null || text.length() < 1)
			return;
		
		Graphics2D g2 = (Graphics2D)g;
		Rectangle rectangle = calcalateCenteredAndCushioned(getBounds(), linkage);

		int arc = 5;

		g2.setColor(DiagramConstants.COLOR_STRESS);
		g2.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, arc, arc);
		g2.drawString(text, rectangle.x+CUSHION, rectangle.y + rectangle.height-CUSHION);
	}
	
	private static final int CUSHION = 5;
	DiagramFactorLink linkage;
	boolean isVisible;
}
