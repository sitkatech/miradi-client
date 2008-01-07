/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ColoredIcon extends AbstractMiradiIcon
{
	public ColoredIcon()
	{
		setColor(Color.BLACK);
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
		return 12;
	}
	
	private static final int MARGIN = 0;
	private Color color;
}
