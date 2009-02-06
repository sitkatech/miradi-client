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
