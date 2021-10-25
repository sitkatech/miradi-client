/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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

	public void clear()
	{
		availableColors = new Vector<Color>();
		
		availableColors.add(Color.black);
		availableColors.add(Color.blue);
		availableColors.add(Color.cyan);
		availableColors.add(Color.magenta);
		availableColors.add(Color.red);
		availableColors.add(Color.yellow);
		availableColors.add(Color.white);

		AppPreferences appPreferences = getAppPreferences();
		if (appPreferences != null)
		{
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_ALERT));
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_CAUTION));
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_OK));
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_GREAT));
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_NOT_KNOWN));
			availableColors.add(appPreferences.getColor(AppPreferences.TAG_COLOR_PLANNED));
		}
		else
		{
			availableColors.add(LEGACY_RED);
			availableColors.add(LEGACY_DARK_YELLOW);
			availableColors.add(LEGACY_LIGHT_GREEN);
			availableColors.add(LEGACY_DARK_GREEN);
			availableColors.add(LEGACY_LIGHT_GREY);
			availableColors.add(LEGACY_DARK_GREY);
			availableColors.add(LEGACY_LIGHT_BLUE);
		}
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

	private AppPreferences getAppPreferences()
	{
		MainWindow mainWindow = EAM.getMainWindow();
		if (mainWindow != null)
			return mainWindow.getAppPreferences();

		return null;
	}

	private Vector<Color> availableColors;
	private static ColorManager singletonInstance;

	// legacy default colors
	public static final Color LEGACY_RED = new Color(0xFD, 0x66, 0x66);
	public static final Color LEGACY_DARK_YELLOW = new Color(0xFC, 0xFC, 0x64);
	public static final Color LEGACY_LIGHT_GREEN = new Color(0xA0, 0xFD, 0x9E);
	public static final Color LEGACY_DARK_GREEN = new Color(0x66, 0xB2, 0x66);
	public static final Color LEGACY_LIGHT_GREY = new Color(0xCC, 0xCC, 0xCC);
	public static final Color LEGACY_DARK_GREY = new Color(0x80, 0x80, 0x80);
	public static final Color LEGACY_LIGHT_BLUE = new Color(0x87, 0xCE, 0xFA);

	// new colors as of Miradi 4.5
	public static final Color RED = new Color(0xc15046);
	public static final Color DARK_YELLOW = new Color(0xefd61d);
	public static final Color LIGHT_GREEN = new Color(0xbae5ba);
	public static final Color DARK_GREEN = new Color(0x50a550);
	public static final Color LIGHT_GREY = new Color(0xCC, 0xCC, 0xCC);
	public static final Color DARK_GREY = new Color(0x82817f);
	public static final Color LIGHT_BLUE = new Color(0x4f95a0);
}
