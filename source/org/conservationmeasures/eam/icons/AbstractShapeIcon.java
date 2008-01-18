/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;


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
