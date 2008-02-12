/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class SlideShowIcon implements Icon
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
		g.setColor(Color.BLACK);
		g.drawRect(x, y , WIDTH/2, HEIGHT/3);
		g.drawRect(x + WIDTH/4, y + HEIGHT/4, WIDTH/2, HEIGHT/3);
		g.drawRect(x + WIDTH/2, y + HEIGHT/2, WIDTH/2, HEIGHT/3);
	}
	
	protected static final int WIDTH = 14;
	protected static final int HEIGHT = 14;
}
