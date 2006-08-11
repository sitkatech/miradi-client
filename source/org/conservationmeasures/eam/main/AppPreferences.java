/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Color;

public class AppPreferences
{
	public AppPreferences()
	{
		interventionColor = new Color(255, 255, 0);
	}
	
	public Color getColor(String tag)
	{
		if(tag.equals(TAG_COLOR_INTERVENTION))
			return interventionColor;

		throw new RuntimeException();
	}
	
	public void setColor(String tag, Color colorToUse)
	{
		if(tag.equals(TAG_COLOR_INTERVENTION))
			interventionColor = colorToUse;
		else
			throw new RuntimeException();
	}
	
	public static final String TAG_COLOR_INTERVENTION = "ColorIntervention";
	
	public Color interventionColor;
}
