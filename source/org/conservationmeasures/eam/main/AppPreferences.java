/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

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
		if(tag.equals(TAG_COLOR_STRATEGY))
			return strategyColor;
		if(tag.equals(TAG_COLOR_ACTIVITIES))
			return activitiesColor;
		if(tag.equals(TAG_COLOR_CONTRIBUTING_FACTOR))
			return contributingFactorColor;
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
		if(tag.equals(TAG_COLOR_STRATEGY))
			strategyColor = colorToUse;
		else if(tag.equals(TAG_COLOR_ACTIVITIES))
			activitiesColor = colorToUse;
		else if(tag.equals(TAG_COLOR_CONTRIBUTING_FACTOR))
			contributingFactorColor = colorToUse;
		else if(tag.equals(TAG_COLOR_DIRECT_THREAT))
			directThreatColor = colorToUse;
		else if(tag.equals(TAG_COLOR_TARGET))
			targetColor = colorToUse;
		else if(tag.equals(TAG_COLOR_SCOPE))
			scopeColor = colorToUse;
		else
			throw new RuntimeException(tag);
	}
	
	public void setBoolean(String tag, boolean state)
	{
		if (tag.equals(TAG_GRID_VISIBLE))
			isGridVisible = state;
		else if (tag.equals(TAG_CELL_RATINGS_VISIBLE))
			isCellRatingsVisible = state;
		else
			throw new RuntimeException(tag);
	}
	
	public boolean getBoolean(String tag)
	{
		if (tag.equals(TAG_GRID_VISIBLE))
			return isGridVisible;
		else if (tag.equals(TAG_CELL_RATINGS_VISIBLE))
			return isCellRatingsVisible;
	
		throw new RuntimeException(tag);
	}
	
	public void setTaggedInt(String tag, int value)
	{
		taggedIntMap.put(tag, new Integer(value));
	}
	
	public int getTaggedInt(String tag)
	{
		Integer value = (Integer)taggedIntMap.get(tag);
		if(value == null)
			return 0;
		return value.intValue();
	}
	
	//TODO: once we are able to save the zoom setting in app pref.
	public void setTaggedDouble(String tag, double value)
	{
		diagramZoomSetting = value;
	//	taggedValueMap.put(tag, new Double(value));
	}
	
	public double getTaggedDouble(String tag)
	{
		return diagramZoomSetting;
//		Double value = (Double)taggedValueMap.get(tag);
//		if(value == null)
//			return 0;
//		return value.doubleValue();
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_COLOR_STRATEGY, strategyColor);
		json.put(TAG_COLOR_ACTIVITIES, activitiesColor);
		json.put(TAG_COLOR_CONTRIBUTING_FACTOR, contributingFactorColor);
		json.put(TAG_COLOR_DIRECT_THREAT, directThreatColor);
		json.put(TAG_COLOR_TARGET, targetColor);
		json.put(TAG_COLOR_SCOPE, scopeColor);
		json.put(TAG_IS_MAXIMIZED, isMaximized);
		json.put(TAG_GRID_VISIBLE, isGridVisible);
		json.put(TAG_CELL_RATINGS_VISIBLE, isCellRatingsVisible);
		
		EnhancedJsonObject taggedIntJson = new EnhancedJsonObject();
		Iterator iter = taggedIntMap.keySet().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			Integer value = (Integer)taggedIntMap.get(key);
			taggedIntJson.put(key, value.intValue());
		}
		json.put(TAG_TAGGED_INTS, taggedIntJson);
		
		return json;
	}
	
	public void loadFrom(EnhancedJsonObject json)
	{
		strategyColor = json.optColor(TAG_COLOR_STRATEGY, DEFAULT_STRATEGY_COLOR);
		activitiesColor = json.optColor(TAG_COLOR_ACTIVITIES, DEFAULT_ACTIVITIES_COLOR);
		contributingFactorColor = json.optColor(TAG_COLOR_CONTRIBUTING_FACTOR, DEFAULT_CONTRIBUTING_FACTOR_COLOR);
		directThreatColor = json.optColor(TAG_COLOR_DIRECT_THREAT, DEFAULT_DIRECT_THREAT_COLOR);
		targetColor = json.optColor(TAG_COLOR_TARGET, DEFAULT_TARGET_COLOR);
		scopeColor = json.optColor(TAG_COLOR_SCOPE, DEFAULT_SCOPE_COLOR);
		
		isGridVisible = json.optBoolean(TAG_GRID_VISIBLE, true);
		isMaximized = json.optBoolean(TAG_IS_MAXIMIZED, false);
		isCellRatingsVisible = json.optBoolean(TAG_CELL_RATINGS_VISIBLE, false);
		
		taggedIntMap = new HashMap();
		EnhancedJsonObject taggedIntJson = json.optJson(TAG_TAGGED_INTS);
		Iterator iter = taggedIntJson.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			int value = taggedIntJson.getInt(key);
			taggedIntMap.put(key, new Integer(value));
		}
	}
	
	public static final String TAG_COLOR_STRATEGY = "ColorIntervention";
	public static final String TAG_COLOR_ACTIVITIES = "ColorActivities";
	public static final String TAG_COLOR_CONTRIBUTING_FACTOR = "ColorIndirectFactor";
	public static final String TAG_COLOR_DIRECT_THREAT = "ColorDirectThreat";
	public static final String TAG_COLOR_TARGET = "ColorTarget";
	public static final String TAG_COLOR_SCOPE = "ColorScope";
	public static final String TAG_IS_MAXIMIZED = "IsMaximized";
	public static final String TAG_GRID_VISIBLE = "GridVisible";
	public static final String TAG_CELL_RATINGS_VISIBLE = "CellRatingsVisible";
	public static final String TAG_TAGGED_INTS = "TaggedInts";
	public static final String TAG_DIAGRAM_ZOOM = "DiagramZoom";
	
	private static final Color DEFAULT_TARGET_COLOR = new Color(153, 255, 153);
	private static final Color DEFAULT_DIRECT_THREAT_COLOR = new Color(255, 150, 150);
	private static final Color DEFAULT_CONTRIBUTING_FACTOR_COLOR = new Color(255, 190, 0);
	private static final Color DEFAULT_STRATEGY_COLOR = new Color(255, 255, 0);
	private static final Color DEFAULT_ACTIVITIES_COLOR = new Color(255, 255, 0);
	private static final Color DEFAULT_SCOPE_COLOR = new Color(0, 255, 0);
	
	public Color strategyColor;
	public Color activitiesColor;
	public Color contributingFactorColor;
	public Color directThreatColor;
	public Color targetColor;
	public Color scopeColor;
	
	private boolean isGridVisible; 
	private boolean isCellRatingsVisible;
	private boolean isMaximized;
	
	
	private double diagramZoomSetting;
	HashMap taggedIntMap;
}
