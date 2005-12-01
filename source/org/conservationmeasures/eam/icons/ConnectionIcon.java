/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class ConnectionIcon implements Icon
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
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine(x+2, y+8, x+14, y+8);
		g.drawLine(x+14, y+8, x+12, y+6);
		g.drawLine(x+14, y+8, x+12, y+10);
		g.setColor(oldColor);
	}
}