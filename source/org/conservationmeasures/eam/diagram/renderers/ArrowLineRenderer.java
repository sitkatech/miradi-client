/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.nodes.DiagramConstants;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;
import org.martus.swing.Utilities;

public class ArrowLineRenderer extends EdgeRenderer
{
	public Component getRendererComponent(JGraph graphToUse, CellView cellView, 
					boolean sel, boolean hasFocus, boolean previewMode)
	{
		ArrowLineRenderer renderer = 
			(ArrowLineRenderer)super.getRendererComponent(graphToUse, cellView, sel, hasFocus, previewMode);
		
		linkage = (DiagramLinkage)cellView.getCell();
		renderer.isVisible = ((DiagramComponent)graphToUse).isLinkageVisible(linkage);
		if(sel)
		{
			renderer.lineWidth = 4;
		}

		return renderer;
	}

	public void paint(Graphics g)
	{
		if(!isVisible)
			return;
		
		super.paint(g);
		drawStress(g);
		
	}

	private void drawStress(Graphics g)
	{
		if(!linkage.getToNode().isTarget())
			return;
		
		String text = "Test Stress";
		if(text == null || text.length() < 1)
			return;
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setClip(EAM.mainWindow.getDiagramComponent().getVisibleRect());
		
		TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
		Rectangle2D textBounds = textLayout.getBounds();
		
		int textWidth = (int)textBounds.getWidth();
		int textHeight = (int)textBounds.getHeight();
		Dimension textSize = new Dimension(textWidth, textHeight);
		Point upperLeftToDrawText = Utilities.center(textSize, getBounds());
		int textX = upperLeftToDrawText.x;
		int textY = upperLeftToDrawText.y;

		int cushion = 5;
		
		int rectX = textX - cushion;
		int rectY = textY - cushion;
		int rectWidth = textSize.width + 2*cushion;
		int rectHeight = textSize.height +2*cushion;

		int arc = 5;

		g2.setColor(DiagramConstants.COLOR_STRESS);
		g2.fillRoundRect(rectX, rectY, rectWidth, rectHeight, arc, arc);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(rectX, rectY, rectWidth, rectHeight, arc, arc);
		g2.drawString(text, textX, textY + textHeight);
	}
	
	boolean isVisible;
	DiagramLinkage linkage;
}
