/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.nodes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.renderers.HexagonRenderer;

public class InsertInterventionIcon implements Icon
{
	public int getIconHeight()
	{
		return 16;
	}

	public int getIconWidth()
	{
		return 16;
	}

	public void paintIcon(Component sample, Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x, y, getIconWidth(), getIconHeight() * 3 / 4);
		Color color = new NodeTypeIntervention().getColor();
		HexagonRenderer hexagon = new HexagonRenderer();
		hexagon.fillShape(g, rect, color);
		hexagon.drawBorder((Graphics2D)g, rect, Color.BLACK);
	}
	
}