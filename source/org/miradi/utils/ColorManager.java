/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.EAM;

import java.awt.*;
import java.util.Vector;

public class ColorManager
{
	private ColorManager()
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
		availableColors = new Vector<Color>();
		
		availableColors.add(Color.black);
		availableColors.add(Color.blue);
		availableColors.add(Color.cyan);
		availableColors.add(Color.magenta);
		availableColors.add(Color.red);
		availableColors.add(Color.yellow);
		availableColors.add(Color.white);
		
		availableColors.add(RED);
		availableColors.add(DARK_YELLOW);
		availableColors.add(LIGHT_GREEN);
		availableColors.add(DARK_GREEN);
		availableColors.add(LIGHT_GREY);
		availableColors.add(DARK_GREY);
		availableColors.add(LIGHT_BLUE);
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

	public static final Color RED = new Color(0xFD, 0x66, 0x66);
	public static final Color DARK_YELLOW = new Color(0xFC, 0xFC, 0x64);
	public static final Color LIGHT_GREEN = new Color(0xA0, 0xFD, 0x9E);
	public static final Color DARK_GREEN = new Color(0x66, 0xB2, 0x66);
	public static final Color LIGHT_GREY = new Color(0xB3, 0xB3, 0xB3);
	public static final Color DARK_GREY = new Color(0x80, 0x80, 0x80);
	public static final Color LIGHT_BLUE = new Color(0x87, 0xCE, 0xFA);
}
