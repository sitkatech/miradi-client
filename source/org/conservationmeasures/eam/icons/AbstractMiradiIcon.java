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
		int plusCenterX = x+getIconWidth() - plusSize()/2;
		int plusCenterY = y + plusSize()/2;
		Color old = g.getColor();
		g.setColor(Color.RED);
		g.drawLine(plusCenterX, y, plusCenterX, y + plusSize());
		g.drawLine(plusCenterX+1, y, plusCenterX+1, y + plusSize());
		g.drawLine(x + getIconWidth() - plusSize(), plusCenterY, x + getIconWidth(), plusCenterY);
		g.drawLine(x + getIconWidth() - plusSize(), plusCenterY+1, x + getIconWidth(), plusCenterY+1);
		g.setColor(old);
	}
	
	int plusSize()
	{
		return getIconHeight() / 3;
	}
}
