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

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class ProjectScopeIcon extends AbstractMiradiIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_SCOPE));
		int rectWidth = getIconWidth() * 4 / 5;
		int rectHeight = getIconHeight() * 7 / 8;
		int left = x + (getIconWidth() - rectWidth)/2;
		int top = y + (getIconHeight() - rectHeight)/2;
		int arcWidth = getIconWidth()/2;
		int arcHeight = getIconHeight()/2;
		g.fillRoundRect(left, top, rectWidth, rectHeight, arcWidth, arcHeight);
		g.setColor(Color.BLACK);
		g.drawRoundRect(left, top, rectWidth, rectHeight, arcWidth, arcHeight);
	}
}
