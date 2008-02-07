/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.icons.AbstractMiradiIcon;
import org.conservationmeasures.eam.icons.ResultsChainIcon;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.utils.Utility;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.martus.util.xml.XmlUtilities;


public abstract class FactorRenderer extends MultilineCellRenderer implements CellViewRenderer
{
	abstract public void fillShape(Graphics g, Rectangle rect, Color color);
	abstract public void fillRawShape(Graphics g, Rectangle rect, Color color);
	abstract public void drawBorder(Graphics2D g2, Rectangle rect, Color color);
	
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		super.getRendererComponent(graphToUse, view, sel, focus, previewMode);
		
		try
		{
			node = (FactorCell)view.getCell();
			DiagramModel model = (DiagramModel)graphToUse.getModel();
			ThreatRatingFramework framework = model.getThreatRatingFramework();
			priority = null;
			if(node.isDirectThreat())
			{
				priority = framework.getThreatThreatRatingValue(node.getWrappedORef());
			}
			if(node.isTarget())
			{
				Target target = (Target)node.getUnderlyingObject();
				String ratingCode = model.getProject().getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
				StatusQuestion question = new StatusQuestion();
				rating = question.findChoiceByCode(ratingCode);
			}
			
			if(node.isStrategy())
			{
				Strategy strategy = (Strategy)node.getUnderlyingObject();
				rating = strategy.getStrategyRating();
				stragetyInResultsChain = shouldDisplayResultsChainIcon(model, strategy);
			}
			
			DiagramComponent diagram = (DiagramComponent)graph;
			isAliased = shouldMarkAsShared(model);
			isOwnedByGroup = node.getDiagramFactor().isCoveredByGroupBox();
			
			EAMGraphCell cell = (EAMGraphCell)view.getCell();
			String formattedLabel =  XmlUtilities.getXmlEncoded(cell.toString());
			formattedLabel = formattedLabel.replace("\n", "<br>");
			setHtmlFormViewerText(getAdditionalHtmlFontTags() + formattedLabel);
			
			indicatorText = null;
			if(diagram.areIndicatorsVisible())
			{
				IdList indicators = node.getUnderlyingObject().getDirectOrIndirectIndicators();
				if(indicators.size() == 1)
					indicatorText = model.getProject().getObjectData(ObjectType.INDICATOR, indicators.get(0), Indicator.TAG_SHORT_LABEL);
				else if(indicators.size() > 1)
					indicatorText = "+";
			}
			
			objectivesText = null;
			if(diagram.areObjectivesVisible())
			{
				if(node.canHaveObjectives())
				{
					IdList objectiveIds = node.getObjectives();
					if(objectiveIds.size() == 1)
						objectivesText = EAM.text("Obj") + " " + model.getProject().getObjectData(ObjectType.OBJECTIVE, objectiveIds.get(0), Objective.TAG_SHORT_LABEL);
					else if(objectiveIds.size() > 1)
						objectivesText = "" + objectiveIds.size() + " " + EAM.text("Objs");
				}
			}
			
			goalsText = null;
			if(diagram.areGoalsVisible())
			{
				if(node.canHaveGoal())
				{
					IdList goalIds = ((Target)node.getUnderlyingObject()).getGoals();
					if(goalIds.size() == 1)
						goalsText = EAM.text("Goal") + " " + model.getProject().getObjectData(ObjectType.GOAL, goalIds.get(0), Goal.TAG_SHORT_LABEL);
					else if(goalIds.size() > 1)
						goalsText = "" + goalIds.size() + " " + EAM.text("Goals");
				}
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return this;
	}
	
	private boolean shouldMarkAsShared(DiagramModel model)
	{
		boolean isSharedInCoceptualModel = model.isSharedInConceptualModel(node.getDiagramFactor());
		if(isSharedInCoceptualModel)
			   return true;

		boolean isResultsChain = model.isResultsChain();
		if(! isResultsChain)
			 return false;
		
		return model.isSharedInResultsChain(node.getDiagramFactor());
	}
	protected String getAdditionalHtmlFontTags()
	{
		String formatted = "<Font "+ getDiagramFactorFontColor() + ">";
		formatted += getDiagramFactorFontStyle();
		if (isAliased)
			return formatted += "<i>";
	
		return formatted;
	}
	
	private String getDiagramFactorFontColor()
	{
		String fontColor = node.getDiagramFactor().getFontColor();
		if (fontColor.length() != 0)
			return fontColor = "color=" + fontColor;
		
		return fontColor;
	}
	
	private String getDiagramFactorFontStyle()
	{
		String fontStyle = node.getDiagramFactor().getFontStyle();
		if (fontStyle.length() != 0)
			return fontStyle;
		
		return fontStyle;
	}

	private boolean shouldDisplayResultsChainIcon(DiagramModel model, Strategy strategy)
	{
		ORefList resultsChains = strategy.getResultsChains();
		if (model.isResultsChain())
			return  false;
		return resultsChains.size() > 0;
	}
	
	public void setRatingBubbleFont(Graphics2D g2)
	{
		g2.setFont(g2.getFont().deriveFont(9.0f).deriveFont(Font.BOLD));
	}
	
	public void paint(Graphics g1)
	{
		int originalHeight = getSize().height;
		int originalWidth = getSize().width; 
		setSize(getSizeWithoutAnnotations(getSize()));
		super.paint(g1);
		setSize(originalWidth, originalHeight);

		Rectangle rect = getNonBorderBounds();
		Graphics2D g2 = (Graphics2D) g1;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if(objectivesText != null)
			drawAnnotationCellRect(rect, g2, new RectangleRenderer(), objectivesText);
		
		if(goalsText != null)
			drawAnnotationCellRect(rect, g2, new EllipseRenderer(), goalsText);
		
		if(indicatorText != null)
			drawIndicator(rect, g2);
		
		if(stragetyInResultsChain)
			drawChainIcon(rect, g2);
		
		if(isOwnedByGroup && !selected)
		{
			g2.setStroke(getOwnedByGroupStroke());
			drawBorder(g2, rect, Color.BLACK);
		}
	}
	
	public Stroke getOwnedByGroupStroke()
	{
		float[] dash = { 5f, 5f };
		return new BasicStroke(borderThickness + 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	}
	
	public static Dimension getSizeWithoutAnnotations(Dimension size)
	{
		return new Dimension(size.width, size.height);
	}
	
	private void drawIndicator(Rectangle rect, Graphics2D g2) 
	{
		if(indicatorText == null)
			return;

		TriangleRenderer indicatorRenderer = new TriangleRenderer();
		Rectangle smallTriangle = getIndicatorRectWithinNode();
		smallTriangle.translate(rect.x, rect.y);
		setPaint(g2, smallTriangle, AppPreferences.INDICATOR_COLOR);
		indicatorRenderer.fillShape(g2, smallTriangle, AppPreferences.INDICATOR_COLOR);
		drawAnnotationBorder(g2, smallTriangle, indicatorRenderer);
		smallTriangle.setLocation(smallTriangle.x, smallTriangle.y + (INDICATOR_HEIGHT / 4));
		
		drawLabel(g2, smallTriangle, indicatorText, smallTriangle.getSize());
	}
	
	private void drawChainIcon(Rectangle rect, Graphics2D g2) 
	{
		AbstractMiradiIcon icon = new ResultsChainIcon();
		Rectangle rectangle = getResultChainRectWithinNode();
		icon.paintIcon(null, g2,rectangle.x, rectangle.y);
	}
	
	private Rectangle getResultChainRectWithinNode()
	{
		return node.getResultChainRectWithinNode();
	}
	
	private Rectangle getIndicatorRectWithinNode()
	{
		return node.getIndicatorRectWithinNode();
	}

	Color getFillColor()
	{
		return node.getColor();
	}
	
	Dimension getInsetDimension()
	{
		return node.getInsetDimension();
	}
	
	Stroke getStroke()
	{
		if(node.getParent() == null)
			return super.getStroke();
		
		float[] dashes = {8.0f, 2.0f};
		return new BasicStroke(borderThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, dashes, 0.0f);
	}

	protected Rectangle getBubbleRect(Rectangle rect)
	{
		Rectangle smallRect = new Rectangle();
		smallRect.x = rect.x;
		if (node.isCause())
			smallRect.y  = rect.y;
		else
			smallRect.y = getSize().height/2 - PRIORITY_HEIGHT/2;
		smallRect.width = PRIORITY_WIDTH;
		smallRect.height = PRIORITY_HEIGHT;
		return smallRect;
	}

	protected void drawRatingBubble(Graphics2D g2, Rectangle rect, Color ratingColor, String ratingText)
	{
		Rectangle smallRect = getBubbleRect(rect);
		fillRawShape(g2, smallRect, ratingColor);
		drawBorder(g2, smallRect, Color.BLACK);
		setRatingBubbleFont(g2);
		g2.setColor(Color.BLACK);
		Utility.drawStringCentered(g2, ratingText, smallRect);
	}
	
	protected void drawCommentTriangle(Graphics2D g2, Rectangle rect, Color color)
	{
		if(node == null || node.getComment().length() <= 0)
			return;
		
		final int triangleInset = 15;
		Polygon triangle = new Polygon();
		triangle.addPoint(getWidth() - triangleInset, 0);
		triangle.addPoint(getWidth(), 0);
		triangle.addPoint(getWidth(), triangleInset);
		triangle.addPoint(getWidth() - triangleInset, 0);
		setPaint(g2, rect, Color.CYAN);
		g2.fill(triangle);
		
		//Temporarly turning off border
		//setPaint(g2, rect, Color.BLACK);
		//g2.drawPolygon(triangle);
	}

	protected static final int PRIORITY_WIDTH = 20;
	protected static final int PRIORITY_HEIGHT = 10;
	protected ChoiceItem priority;
	protected FactorCell node;
	protected ChoiceItem rating;
	private String indicatorText;
	private String objectivesText;
	private String goalsText;
	boolean stragetyInResultsChain;
	boolean isAliased;
	boolean isOwnedByGroup;
}
