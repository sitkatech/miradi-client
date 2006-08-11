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
		indirectFactorColor = new Color(255, 190, 0);
		directThreatColor = new Color(255, 150, 150);
		targetColor = new Color(153, 255, 153);
	}
	
	public Color getColor(String tag)
	{
		if(tag.equals(TAG_COLOR_INTERVENTION))
			return interventionColor;
		if(tag.equals(TAG_COLOR_INDIRECT_FACTOR))
			return indirectFactorColor;
		if(tag.equals(TAG_COLOR_DIRECT_THREAT))
			return directThreatColor;
		if(tag.equals(TAG_COLOR_TARGET))
			return targetColor;
			
		throw new RuntimeException(tag);
	}
	
	public void setColor(String tag, Color colorToUse)
	{
		if(tag.equals(TAG_COLOR_INTERVENTION))
			interventionColor = colorToUse;
		else if(tag.equals(TAG_COLOR_INDIRECT_FACTOR))
			indirectFactorColor = colorToUse;
		else if(tag.equals(TAG_COLOR_DIRECT_THREAT))
			directThreatColor = colorToUse;
		else if(tag.equals(TAG_COLOR_TARGET))
			targetColor = colorToUse;
		else
			throw new RuntimeException(tag);
	}
	
	public static final String TAG_COLOR_INTERVENTION = "ColorIntervention";
	public static final String TAG_COLOR_INDIRECT_FACTOR = "ColorIndirectFactor";
	public static final String TAG_COLOR_DIRECT_THREAT = "ColorDirectThreat";
	public static final String TAG_COLOR_TARGET = "ColorTarget";
	
	
	public Color interventionColor;
	public Color indirectFactorColor;
	public Color directThreatColor;
	public Color targetColor;
}
