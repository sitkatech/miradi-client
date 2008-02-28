/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TaskIcon extends AbstractMiradiIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(getIconColor());
		int rectWidth = getIconWidth() * 7 / 8;
		int rectHeight = getIconHeight() / 2;
		int left = x + (getIconWidth() - rectWidth)/2;
		int top = y + (getIconHeight() - rectHeight)/2;
		int arcWidth = getIconWidth()/2;
		int arcHeight = getIconHeight()/2;
		g.fillRoundRect(left, top, rectWidth, rectHeight, arcWidth, arcHeight);
		g.setColor(Color.BLACK);
		g.drawRoundRect(left, top, rectWidth, rectHeight, arcWidth, arcHeight);
	}

	protected Color getIconColor()
	{
		return TASK_COLOR;
	}

	public static final Color TASK_COLOR = new Color(0xdd, 0xdd, 0xdd);
}
