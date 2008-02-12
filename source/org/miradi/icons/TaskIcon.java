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

		g.setColor(TASK_COLOR);
		g.drawOval(x + getIconWidth()/4, y + getIconHeight()/4, getIconWidth()/2, getIconHeight()/2);
		g.fillOval(x + getIconWidth()/4, y + getIconHeight()/4, getIconWidth()/2, getIconHeight()/2);
	}

	public static final Color TASK_COLOR = Color.BLACK;
}
