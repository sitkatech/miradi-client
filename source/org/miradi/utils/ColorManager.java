/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Color;
import java.util.Vector;

import org.miradi.main.EAM;

public class ColorManager
{
	public ColorManager()
	{
		clear();
	}
	
	public static ColorManager instance()
	{
		if (singletonInstance == null)
			singletonInstance = new ColorManager();
		
		return singletonInstance;
	}	

	private void clear()
	{
		availableColors = new Vector();
		
		availableColors.add(Color.black);
		availableColors.add(Color.blue);
		availableColors.add(Color.cyan);
		availableColors.add(Color.magenta);
		availableColors.add(Color.red);
		availableColors.add(Color.yellow);
		availableColors.add(Color.white);
		
		availableColors.add(DARK_GREEN);
		availableColors.add(LIGHT_GREEN);
		availableColors.add(DARK_YELLOW);
	}
	
	public int getColorIndex(Color color)
	{
		int indexOfColor = availableColors.indexOf(color);
		if (indexOfColor < 0)
			EAM.logError("No Available color found");
		
		return indexOfColor;
	}
	
	public Vector<Color> getAvailableColors()
	{
		return availableColors;
	}
		
	private Vector<Color> availableColors;
	private static ColorManager singletonInstance;
	
	public static final Color DARK_YELLOW = new Color(255, 230, 0);
	public static final Color LIGHT_GREEN = new Color(128, 255, 0); 
	public static final Color DARK_GREEN = new Color(0, 160, 0);
}
