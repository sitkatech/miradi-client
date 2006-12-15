/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class TaskIcon implements Icon
{
	public int getIconHeight()
	{
		return 16;
	}

	public int getIconWidth()
	{
		return 16;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(TASK_COLOR);
		g.drawOval(x/4, y/4, getIconWidth()/2, getIconHeight()/2);
		g.fillOval(x/4, y/4, getIconWidth()/2, getIconHeight()/2);
	}

	public static final Color TASK_COLOR = Color.BLACK;
}
