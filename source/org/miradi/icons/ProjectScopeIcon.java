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

		g.setColor(EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_SCOPE_BOX));
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
