/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class ProjectScopeRenderer extends MultilineCellRenderer
{
	Color getFillColor()
	{
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_SCOPE);
	}

	public void paint(Graphics g1)
	{
		super.paint(g1);

		Graphics2D g2 = (Graphics2D) g1;
		Rectangle rect = getNonBorderBounds();
		drawProjectScopeVision(g2, rect);

	}

	private void drawProjectScopeVision(Graphics2D g2, Rectangle rect)
	{
		if(vision == null || vision.length() == 0)
			return;
		Rectangle visionRect = new Rectangle();
		visionRect.x = getAnnotationX(rect.x);
		visionRect.y = rect.y + textMargin;
		visionRect.width = getAnnotationsWidth(rect.width);
		visionRect.height = getAnnotationsHeight();
		drawAnnotation(visionRect, g2, new RoundRectangleRenderer(), vision);
	}
	
}
