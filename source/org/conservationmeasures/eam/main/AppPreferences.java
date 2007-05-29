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
import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

//TODO: look into replace individual fields with MapList or hash maps
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
		if(tag.equals(TAG_COLOR_INTERMEDIATE_RESULT))
			return intermediateResultColor;
		if(tag.equals(TAG_COLOR_THREAT_REDUCTION_RESULT))
			return threatReductionResultColor;
			
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
		else if(tag.equals(TAG_COLOR_INTERMEDIATE_RESULT))
			intermediateResultColor = colorToUse;
		else if(tag.equals(TAG_COLOR_THREAT_REDUCTION_RESULT))
			threatReductionResultColor = colorToUse;
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
		if(tag.equals(TAG_WIZARD_FONT_SIZE))
			wizardFontSize = value;
		else
			taggedIntMap.put(tag, new Integer(value));
	}
	
	public int getTaggedInt(String tag)
	{
		if(tag.equals(TAG_WIZARD_FONT_SIZE))
			return wizardFontSize;
		
		Integer value = (Integer)taggedIntMap.get(tag);
		if(value == null)
			return 0;
		return value.intValue();
	}
	
	public void setTaggedString(String tag, String value)
	{
		if(tag.equals(TAG_WIZARD_FONT_FAMILY))
			wizardFontFamily = value;
		else
			taggedStringMap.put(tag, new String(value));
	}	
	
	public String getTaggedString(String tag)
	{
		if(tag.equals(TAG_WIZARD_FONT_FAMILY))
			return wizardFontFamily;
		
		
		String value = (String) taggedStringMap.get(tag);
		if(value == null)
			return "";
		return value;
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
		json.put(TAG_COLOR_INTERMEDIATE_RESULT, intermediateResultColor);
		json.put(TAG_COLOR_THREAT_REDUCTION_RESULT, threatReductionResultColor);
		json.put(TAG_IS_MAXIMIZED, isMaximized);
		json.put(TAG_GRID_VISIBLE, isGridVisible);
		json.put(TAG_CELL_RATINGS_VISIBLE, isCellRatingsVisible);
		
		json.put(TAG_WIZARD_FONT_FAMILY, wizardFontFamily);
		json.put(TAG_WIZARD_FONT_SIZE, Integer.toString(wizardFontSize));
		
		json.put(TAG_TAGGED_INTS, putIntegerMapToJson());
		json.put(TAG_TAGGED_STRINGS, putStringMapToJson());
		
		return json;
	}
	
	private EnhancedJsonObject putStringMapToJson()
	{
		EnhancedJsonObject taggedStringJson = new EnhancedJsonObject();
		Iterator iter = taggedStringMap.keySet().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			String value = (String)taggedStringMap.get(key);
			taggedStringJson.put(key, value);
		}
		return taggedStringJson;
	}

	private EnhancedJsonObject putIntegerMapToJson()
	{
		EnhancedJsonObject taggedIntJson = new EnhancedJsonObject();
		Iterator iter = taggedIntMap.keySet().iterator();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			Integer value = (Integer)taggedIntMap.get(key);
			taggedIntJson.put(key, value.intValue());
		}
		return taggedIntJson;
	}
	
	public void loadFrom(EnhancedJsonObject json)
	{
		strategyColor = json.optColor(TAG_COLOR_STRATEGY, DiagramConstants.DEFAULT_STRATEGY_COLOR);
		activitiesColor = json.optColor(TAG_COLOR_ACTIVITIES, DiagramConstants.DEFAULT_ACTIVITIES_COLOR);
		contributingFactorColor = json.optColor(TAG_COLOR_CONTRIBUTING_FACTOR, DiagramConstants.DEFAULT_CONTRIBUTING_FACTOR_COLOR);
		directThreatColor = json.optColor(TAG_COLOR_DIRECT_THREAT, DiagramConstants.DEFAULT_DIRECT_THREAT_COLOR);
		targetColor = json.optColor(TAG_COLOR_TARGET, DiagramConstants.DEFAULT_TARGET_COLOR);
		scopeColor = json.optColor(TAG_COLOR_SCOPE, DiagramConstants.DEFAULT_SCOPE_COLOR);
		intermediateResultColor = json.optColor(TAG_COLOR_INTERMEDIATE_RESULT, DiagramConstants.DEFAULT_INTERMEDIATE_RESULT_COLOR);
		threatReductionResultColor = json.optColor(TAG_COLOR_THREAT_REDUCTION_RESULT, DiagramConstants.DEFAULT_THREAT_REDUCTION_RESULT_COLOR);
		
		isGridVisible = json.optBoolean(TAG_GRID_VISIBLE, true);
		isMaximized = json.optBoolean(TAG_IS_MAXIMIZED, false);
		isCellRatingsVisible = json.optBoolean(TAG_CELL_RATINGS_VISIBLE, false);
		
		wizardFontFamily = json.optString(TAG_WIZARD_FONT_FAMILY);
		wizardFontSize = json.optInt(TAG_WIZARD_FONT_SIZE);
		
		taggedIntMap = loadTagIntegerMap(json);
		taggedStringMap = loadTagStringMap(json);
	}

	
	private HashMap loadTagStringMap(EnhancedJsonObject json)
	{
		HashMap map = new HashMap();
		EnhancedJsonObject taggedStringJson = json.optJson(TAG_TAGGED_STRINGS);
		Iterator iter = taggedStringJson.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			String value = taggedStringJson.getString(key);
			map.put(key, value);
		}
		return map;
	}
	
	
	private HashMap loadTagIntegerMap(EnhancedJsonObject json)
	{
		HashMap map = new HashMap();
		EnhancedJsonObject taggedIntJson = json.optJson(TAG_TAGGED_INTS);
		Iterator iter = taggedIntJson.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			int value = taggedIntJson.getInt(key);
			map.put(key, new Integer(value));
		}
		return map;
	}
	
	public static final String TAG_COLOR_STRATEGY = "ColorIntervention";
	public static final String TAG_COLOR_ACTIVITIES = "ColorActivities";
	public static final String TAG_COLOR_CONTRIBUTING_FACTOR = "ColorIndirectFactor";
	public static final String TAG_COLOR_DIRECT_THREAT = "ColorDirectThreat";
	public static final String TAG_COLOR_TARGET = "ColorTarget";
	public static final String TAG_COLOR_SCOPE = "ColorScope";
	public static final String TAG_COLOR_INTERMEDIATE_RESULT = "ColorIntermediateResult";
	public static final String TAG_COLOR_THREAT_REDUCTION_RESULT = "ColorThreatReductionResult";
	public static final String TAG_IS_MAXIMIZED = "IsMaximized";
	public static final String TAG_GRID_VISIBLE = "GridVisible";
	public static final String TAG_CELL_RATINGS_VISIBLE = "CellRatingsVisible";
	public static final String TAG_TAGGED_INTS = "TaggedInts";
	public static final String TAG_TAGGED_STRINGS = "TaggedStrings";
	public static final String TAG_DIAGRAM_ZOOM = "DiagramZoom";
	
	public static final String TAG_WIZARD_FONT_FAMILY = "WizardFontFamily";
	public static final String TAG_WIZARD_FONT_SIZE = "WizardFontSize";
	
	public Color strategyColor;
	public Color activitiesColor;
	public Color contributingFactorColor;
	public Color directThreatColor;
	public Color targetColor;
	public Color scopeColor;
	public Color intermediateResultColor;
	public Color threatReductionResultColor;
	
	private boolean isGridVisible; 
	private boolean isCellRatingsVisible;
	private boolean isMaximized;
	
	private double diagramZoomSetting;
	
	private String wizardFontFamily;
	private int wizardFontSize;
	
	HashMap taggedIntMap;
	HashMap taggedStringMap;
}
