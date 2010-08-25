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

package org.miradi.main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import javax.swing.JDialog;

public class HitTest
{
	public static void main(String[] args)
	{
		final int startX = 0;
		final int startY = 0;
		final int endX = 100;
		final int endY = 100;
		final int curveRadius = 10;
		
		JDialog dialog = new JDialog();
		dialog.setVisible(true);
		Graphics2D g2 = (Graphics2D) dialog.getGraphics();
		GeneralPath shape = new GeneralPath();
		shape.moveTo(startX, startY);
		shape.lineTo(startX, endY - curveRadius);
		shape.quadTo(startX, endY, startX + curveRadius, endY);
		shape.lineTo(endX, endY);
		System.out.println("Should be true:  " + g2.hit(new Rectangle(50, 50, 100, 100), shape, true));
		System.out.println("Should be false: " + g2.hit(new Rectangle(50, 50, 10, 10), shape, true));
		dialog.dispose();
	}
}
