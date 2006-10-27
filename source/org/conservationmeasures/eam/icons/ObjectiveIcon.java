/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;


public class ObjectiveIcon extends DesireIcon
{

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(MultilineNodeRenderer.ANNOTATIONS_COLOR);
		g.fillRect(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
		g.setColor(Color.BLACK);
		g.drawRect(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
	}
}
