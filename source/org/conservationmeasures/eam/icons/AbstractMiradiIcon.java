/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public abstract class AbstractMiradiIcon implements Icon
{
	public int getIconHeight()
	{
		return 16;
	}

	public int getIconWidth()
	{
		return 16;
	}
	
	void drawCreatePlus(Component c, Graphics g, int x, int y)
	{
		int plusRight = x + getIconWidth();
		int plusLeft = plusRight - plusSize() - 1;
		int plusTop = y;
		int plusBottom = plusTop + plusSize() + 1;

		int plusCenterX = plusLeft + plusSize()/2; 
		int plusCenterY = plusTop + plusSize()/2;
		
		Color old = g.getColor();
		g.setColor(Color.RED);
		g.drawLine(plusCenterX, plusTop, plusCenterX, plusBottom);
		g.drawLine(plusCenterX+1, plusTop, plusCenterX+1, plusBottom);
		g.drawLine(plusLeft, plusCenterY, plusRight, plusCenterY);
		g.drawLine(plusLeft, plusCenterY+1, plusRight, plusCenterY+1);
		g.setColor(old);
	}
	
	int plusSize()
	{
		return 6;
	}
}
