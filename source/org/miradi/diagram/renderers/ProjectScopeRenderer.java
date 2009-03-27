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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.ScopeBoxCell;

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
	
	@Override
	Dimension getInsetDimension()
	{
		Dimension insetDimension = new Dimension(CORNER_SIZE/2, 0);
		return insetDimension;
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
		if(cell.isScopeBox())
		{
			projectScopeBox = (ScopeBoxCell)(view.getCell());
			vision = projectScopeBox.getVision();
			shortScopeHeight = ((ScopeBoxCell)(view.getCell())).getShortScopeHeight();
		}
		
		return super.getRendererComponent(graphToUse, view, sel, focus, previewMode);
	}
	
	private ScopeBoxCell projectScopeBox;
	private String vision;
	private int shortScopeHeight;
}
