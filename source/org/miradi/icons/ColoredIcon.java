/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ColoredIcon extends AbstractMiradiIcon
{
	public ColoredIcon()
	{
		this(Color.BLACK);
	}
	
	public ColoredIcon(Color colorToUse)
	{
		color = colorToUse;
	}
	
	public void setColor(Color colorToUse)
	{
		color = colorToUse;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(color);
		g.fillRect(x + MARGIN, y + MARGIN, getIconWidth() - MARGIN*2, getIconHeight() - MARGIN*2);
	}

	public int getIconWidth()
	{
		return super.getIconWidth() / 2;
	}
	
	private static final int MARGIN = 0;
	private Color color;
}
