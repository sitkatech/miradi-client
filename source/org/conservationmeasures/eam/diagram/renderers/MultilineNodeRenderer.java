/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Portions of this file are based on work with this copyright:
 * Copyright (c) 2001-2004, Gaudenz Alder
 * All rights reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of JGraph nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.Indicator;
import org.conservationmeasures.eam.diagram.nodes.ThreatPriority;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;


public abstract class MultilineNodeRenderer extends MultilineCellRenderer implements CellViewRenderer
{
	abstract Dimension getInsetDimension();
	abstract public void fillShape(Graphics g, Rectangle rect, Color color);
	abstract public void drawBorder(Graphics2D g2, Rectangle rect, Color color);
	
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		super.getRendererComponent(graphToUse, view, sel, focus, previewMode);

		node = (DiagramNode)view.getCell();
		isVisible = ((DiagramComponent)graphToUse).isNodeVisible(node);
		priority = node.getThreatPriority();
		return this;
	}
	
	public void paint(Graphics g1)
	{
		if(!isVisible)
			return;

		int originalHeight = getSize().height;
		int originalWidth = getSize().width; 
		setSize(getSizeWithoutAnnotations(getSize(), node.getAnnotationRows()));
		super.paint(g1);
		setSize(originalWidth, originalHeight);

		Rectangle rect = getNonBorderBounds();
		Graphics2D g2 = (Graphics2D) g1;

		drawAnnotation(rect, g2, node.getObjectives());
		drawAnnotation(rect, g2, node.getGoals());
		drawIndicator(rect, g2);
	}
	
	public static Dimension getSizeWithoutAnnotations(Dimension size, int annotationCount)
	{
		return new Dimension(size.width, size.height - (annotationCount * (INDICATOR_HEIGHT / 2)));
	}

	

	private void drawIndicator(Rectangle rect, Graphics2D g2) 
	{
		Indicator indicator = node.getIndicator();
		if(indicator != null && indicator.hasIndicator())
		{
			TriangleRenderer indicatorRenderer = new TriangleRenderer();
			Rectangle annotationsRectangle = getAnnotationsRect(rect, 1);
			
			Rectangle smallTriangle = new Rectangle();
			smallTriangle.x = annotationsRectangle.x-INDICATOR_WIDTH;
			smallTriangle.y = annotationsRectangle.y;
			smallTriangle.width = INDICATOR_WIDTH;
			smallTriangle.height = INDICATOR_HEIGHT;
			setPaint(g2, smallTriangle, indicator.getColor());
			indicatorRenderer.fillShape(g2, smallTriangle, indicator.getColor());
			drawBoarder(g2, smallTriangle, indicatorRenderer);
			smallTriangle.setLocation(smallTriangle.x, smallTriangle.y + (INDICATOR_HEIGHT / 4));
			drawLabel(g2, smallTriangle, indicator.toString(), smallTriangle.getSize());
		}
	}

	int getAnnotationLeftOffset()
	{
		if(node.getIndicator() == null || !node.getIndicator().hasIndicator())
			return INDICATOR_WIDTH / 2;
		
		int indicatorOffset = getInsetDimension().width - INDICATOR_WIDTH / 2;
		if(indicatorOffset < 0)
			indicatorOffset = 0;
		int annotationOffset = indicatorOffset + INDICATOR_WIDTH;
		return annotationOffset;
	}
	
	int getAnnotationRightInset()
	{
		return INDICATOR_WIDTH / 2;
	}


	
	ThreatPriority priority;
	DiagramNode node;
}
