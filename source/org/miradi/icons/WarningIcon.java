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

public class WarningIcon extends AbstractTriangleIcon
{
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		super.paintIcon(c, g, x, y);
		
		int exclamationMarkX = (getIconWidth() / 2) + x;
		int exclamationMarkY = getIconHeight() + y;
		
		final int EXCLAMATION_MAIN_BODY_START_Y = exclamationMarkY - 4;
		final int EXCLAMATION_MAIN_BODY_END_Y = exclamationMarkY - 9;
		g.drawLine(exclamationMarkX, EXCLAMATION_MAIN_BODY_START_Y, exclamationMarkX, EXCLAMATION_MAIN_BODY_END_Y);
		
		final int EXCLAMATION_POINT_START_Y = exclamationMarkY - 2;
		g.drawLine(exclamationMarkX, EXCLAMATION_POINT_START_Y, exclamationMarkX, EXCLAMATION_POINT_START_Y);
	}
	
	@Override
	protected Color getFillColor()
	{
		return Color.YELLOW;
	}
}
