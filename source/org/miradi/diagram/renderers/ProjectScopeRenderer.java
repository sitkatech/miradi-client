/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.ProjectScopeBox;

public class ProjectScopeRenderer extends MultilineCellRenderer
{
	Color getFillColor()
	{
		return projectScopeBox.getColor();
	}

	public void paint(Graphics g1)
	{
		super.paint(g1);

		Graphics2D g2 = (Graphics2D) g1;
		Rectangle rect = getNonBorderBounds();
		drawProjectScopeVision(g2, rect);

	}
	
	protected Rectangle getMainTextRect()
	{
		Rectangle textAreaRect = super.getMainTextRect();
		textAreaRect.height = shortScopeHeight;
		
		return textAreaRect;
	}

	private void drawProjectScopeVision(Graphics2D g2, Rectangle rect)
	{
		if(vision == null || vision.length() == 0)
			return;
		Rectangle visionRect = new Rectangle();
		visionRect.x = getAnnotationX(rect.x);
		visionRect.y = rect.y + shortScopeHeight;
		visionRect.width = getAnnotationsWidth(rect.width);
		visionRect.height = getAnnotationsHeight();
		drawAnnotation(visionRect, g2, new RoundRectangleRenderer(), vision);
	}
	
	
	public Component getRendererComponent(JGraph graphToUse, CellView view,
			boolean sel, boolean focus, boolean previewMode)
	{
		EAMGraphCell cell = (EAMGraphCell)view.getCell();
		if(cell.isProjectScope())
		{
			projectScopeBox = (ProjectScopeBox)(view.getCell());
			vision = projectScopeBox.getVision();
			shortScopeHeight = ((ProjectScopeBox)(view.getCell())).getShortScopeHeight();
		}
		
		return super.getRendererComponent(graphToUse, view, sel, focus, previewMode);
	}
	
	private ProjectScopeBox projectScopeBox;
	private String vision;
	private int shortScopeHeight;
}
