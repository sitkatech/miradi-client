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

public class WarningIcon extends AbstractTriangleIcon
{
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		super.paintIcon(c, g, x, y);
		
		Graphics2D g2 = (Graphics2D) g;
		float exclamationMarkX = (float) (x + 6.5);
		float exclamationMarkY = y + 14;
		
		g2.drawString(EXCLAMATION_MARK, exclamationMarkX, exclamationMarkY);
	}
	
	@Override
	protected Color getFillColor()
	{
		return Color.YELLOW;
	}
	
	private static final String EXCLAMATION_MARK = "!";
}
