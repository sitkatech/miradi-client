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

public class AccountCodeIcon implements Icon
{
	public int getIconHeight()
	{
		return HEIGHT;
	}

	public int getIconWidth()
	{
		return WIDTH;
	}
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(new Color(237, 185, 0));
		g.fillOval(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
		g.setColor(Color.BLACK);
		g.drawOval(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
	}
	
	protected static final int WIDTH = 16;
	protected static final int HEIGHT = 16;
}