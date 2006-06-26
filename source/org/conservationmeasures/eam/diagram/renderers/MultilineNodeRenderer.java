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

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectiveIds;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;


public abstract class MultilineNodeRenderer extends MultilineCellRenderer implements CellViewRenderer
{
	abstract public void fillShape(Graphics g, Rectangle rect, Color color);
	abstract public void drawBorder(Graphics2D g2, Rectangle rect, Color color);
	
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		super.getRendererComponent(graphToUse, view, sel, focus, previewMode);

		node = (DiagramNode)view.getCell();
		DiagramModel model = (DiagramModel)graphToUse.getModel();
		ThreatRatingFramework framework = model.getThreatRatingFramework();
		priority = null;
		if(node.isDirectThreat())
			priority = new ThreatRatingValue(framework.getThreatThreatRatingValue(node.getId()));
		else if(node.isTarget())
			priority = new ThreatRatingValue(framework.getTargetThreatRatingValue(node.getId()));
		indicator = ((DiagramComponent)graphToUse).getProject().getIndicatorPool().find(node.getIndicatorId());
		return this;
	}
	
	public void paint(Graphics g1)
	{
		int originalHeight = getSize().height;
		int originalWidth = getSize().width; 
		setSize(getSizeWithoutAnnotations(getSize(), node.getAnnotationRows()));
		super.paint(g1);
		setSize(originalWidth, originalHeight);

		Rectangle rect = getNonBorderBounds();
		Graphics2D g2 = (Graphics2D) g1;

		DiagramModel model = (DiagramModel)graph.getModel();
		ObjectiveIds objectives = node.getObjectives();
		if(objectives.hasAnnotation())
		{
			int objectiveId = objectives.getId(0);
			Objective objective = model.getObjectiveById(objectiveId);
			if(objective != null)
				drawAnnotation(rect, g2, EAM.text("Label|Obj ") + objective.getShortLabel());
		}
		GoalIds goals = node.getGoals();
		if(goals.hasAnnotation())
		{
			int goalId = goals.getId(0);
			Goal goal = model.getGoalById(goalId);
			if(goal != null && goal.hasAnnotation())
				drawAnnotation(rect, g2, goal.toString());
		}
		drawIndicator(rect, g2);
	}
	
	public static Dimension getSizeWithoutAnnotations(Dimension size, int annotationCount)
	{
		return new Dimension(size.width, size.height - (annotationCount * (INDICATOR_HEIGHT / 2)));
	}

	

	private void drawIndicator(Rectangle rect, Graphics2D g2) 
	{
		if(indicator == null)
			return;
		
		int indicatorId = indicator.getId();
		if(indicatorId == IdAssigner.INVALID_ID)
			return;
		
		TriangleRenderer indicatorRenderer = new TriangleRenderer();
		Rectangle smallTriangle = getIndicatorRectWithinNode();
		smallTriangle.translate(rect.x, rect.y);
		setPaint(g2, smallTriangle, INDICATOR_COLOR);
		indicatorRenderer.fillShape(g2, smallTriangle, INDICATOR_COLOR);
		drawAnnotationBorder(g2, smallTriangle, indicatorRenderer);
		smallTriangle.setLocation(smallTriangle.x, smallTriangle.y + (INDICATOR_HEIGHT / 4));
		drawLabel(g2, smallTriangle, indicator.getShortLabel(), smallTriangle.getSize());
	}
	
	private Rectangle getIndicatorRectWithinNode()
	{
		return node.getIndicatorRectWithinNode();
	}
	
	Rectangle getAnnotationsRect(Rectangle rect, int numberLines) 
	{
		Rectangle annotationsRect = node.getAnnotationsRect();
		annotationsRect.translate(rect.x, rect.y);
		return annotationsRect;
	}
	
	Color getFillColor()
	{
		return node.getColor();
	}
	
	Dimension getInsetDimension()
	{
		return node.getInsetDimension();
	}
	

	private static final Color LIGHT_PURPLE = new Color(204,153,255);
	private static final Color INDICATOR_COLOR = LIGHT_PURPLE;

	
	ThreatRatingValue priority;
	Indicator indicator;
	DiagramNode node;
}
