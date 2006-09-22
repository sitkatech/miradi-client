/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.database.JSONFile;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class AppPreferences
{
	public AppPreferences()
	{
		clear();
	}

	private void clear()
	{
		loadFrom(new EnhancedJsonObject());
	}
	
	public void load(File preferencesFile) throws IOException, ParseException
	{
		clear();
		if(!preferencesFile.exists())
			return;
		EnhancedJsonObject json = JSONFile.read(preferencesFile);
		loadFrom(json);
	}
	
	public void save(File preferencesFile) throws IOException
	{
		JSONFile.write(preferencesFile, toJson());
	}
	
	public void setIsMaximized(boolean state)
	{
		isMaximized = state;
	}
	
	public boolean getIsMaximized()
	{
		return isMaximized;
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
		if(tag.equals(TAG_COLOR_SCOPE))
			return scopeColor;
			
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
		else if(tag.equals(TAG_COLOR_SCOPE))
			scopeColor = colorToUse;
		else
			throw new RuntimeException(tag);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_COLOR_INTERVENTION, interventionColor);
		json.put(TAG_COLOR_INDIRECT_FACTOR, indirectFactorColor);
		json.put(TAG_COLOR_DIRECT_THREAT, directThreatColor);
		json.put(TAG_COLOR_TARGET, targetColor);
		json.put(TAG_COLOR_SCOPE, scopeColor);
		json.put(TAG_IS_MAXIMIZED, isMaximized);
		
		return json;
	}
	
	public void loadFrom(EnhancedJsonObject json)
	{
		interventionColor = json.optColor(TAG_COLOR_INTERVENTION, DEFAULT_INTERVENTION_COLOR);
		indirectFactorColor = json.optColor(TAG_COLOR_INDIRECT_FACTOR, DEFAULT_INDIRECT_FACTOR_COLOR);
		directThreatColor = json.optColor(TAG_COLOR_DIRECT_THREAT, DEFAULT_DIRECT_THREAT_COLOR);
		targetColor = json.optColor(TAG_COLOR_TARGET, DEFAULT_TARGET_COLOR);
		scopeColor = json.optColor(TAG_COLOR_SCOPE, DEFAULT_SCOPE_COLOR);
		
		isMaximized = json.optBoolean(TAG_IS_MAXIMIZED, false);
	}
	
	public static final String TAG_COLOR_INTERVENTION = "ColorIntervention";
	public static final String TAG_COLOR_INDIRECT_FACTOR = "ColorIndirectFactor";
	public static final String TAG_COLOR_DIRECT_THREAT = "ColorDirectThreat";
	public static final String TAG_COLOR_TARGET = "ColorTarget";
	public static final String TAG_COLOR_SCOPE = "ColorScope";
	public static final String TAG_IS_MAXIMIZED = "IsMaximized";
	
	private static final Color DEFAULT_TARGET_COLOR = new Color(153, 255, 153);
	private static final Color DEFAULT_DIRECT_THREAT_COLOR = new Color(255, 150, 150);
	private static final Color DEFAULT_INDIRECT_FACTOR_COLOR = new Color(255, 190, 0);
	private static final Color DEFAULT_INTERVENTION_COLOR = new Color(255, 255, 0);
	private static final Color DEFAULT_SCOPE_COLOR = new Color(0, 255, 0);
	
	public Color interventionColor;
	public Color indirectFactorColor;
	public Color directThreatColor;
	public Color targetColor;
	public Color scopeColor;
	
	boolean isMaximized;
}
