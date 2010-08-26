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
import java.awt.Rectangle;

import org.miradi.diagram.renderers.FactorRenderer;


abstract public class AbstractShapeIcon extends AbstractMiradiIcon
{
	
	abstract FactorRenderer getRenderer();
	abstract Color getIconColor();
	
	public void paintIcon(Component sample, Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x, y, getIconWidth(), getIconHeight() * 3 / 4);
		Color color = getIconColor();
		FactorRenderer renderer = getRenderer();
		renderer.fillShape(g, rect, color);
		renderer.drawBorder((Graphics2D)g, rect, Color.BLACK);
	}
}
