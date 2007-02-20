/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextLayout;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.Utilities;

public class Utility
{
	public static double convertStringToDouble(String raw)
	{
		if (raw.trim().length() == 0)
			return 0;
		
		double newDouble = 0;
		try
		{
			 newDouble = new Double(raw).doubleValue();
		}
		catch (NumberFormatException e)
		{
			EAM.logException(e);
		}
		
		return newDouble; 
	}
	
	
	public static void drawStringCentered(Graphics2D g2, String text, Rectangle graphBounds)
	{
		TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
		Rectangle textBounds = textLayout.getBounds().getBounds();
		Point p =  Utilities.center(textBounds.getSize(), graphBounds.getBounds().getBounds());
		g2.drawString(text, p.x,  p.y+ textBounds.height);
	}
}
