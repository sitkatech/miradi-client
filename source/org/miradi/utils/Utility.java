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
package org.miradi.utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.martus.swing.Utilities;
import org.miradi.main.EAM;

public class Utility
{
	public static Point convertPoint2DToPoint(Point2D point2d)
	{
		int x = (int) point2d.getX();
		int y = (int) point2d.getY();
		
		return new Point(x, y);
	}
	
	public static Point2D convertToPoint2D(Point point)
	{
		double x = point.getX();
		double y = point.getY();
		
		return new Point2D.Double(x, y);
	}
	
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
	

	static public void copy(InputStream inputStream, OutputStream outputStream) throws Exception
	{
		byte[] buffer = new byte[1024];
		int numRead;
		long numWritten = 0;
		while ((numRead = inputStream.read(buffer)) != -1) 
		{
			outputStream.write(buffer, 0, numRead);
			numWritten += numRead;
		}
	}
	
	
	static public String getFileNameWithoutExtension(String name)
	{
		String fileName = new File(name).getName();
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		
		return fileName.substring(0, lastDotAt);
	}
	
	public static int[] convertToIntArray(Vector<Integer> values)
	{
		int[] convertedValuesList = new int [values.size()];
		for (int i = 0; i < convertedValuesList.length; ++i)
		{
			convertedValuesList[i] = values.get(i);
		}
		
		return convertedValuesList;
	}
}
