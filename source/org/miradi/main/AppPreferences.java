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
package org.miradi.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.miradi.database.JSONFile;
import org.miradi.diagram.DiagramConstants;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

//TODO: look into replace individual fields with MapList or hash maps
public class AppPreferences
{
	public AppPreferences() throws Exception
	{
		clear();
	}

	private void clear() throws Exception
	{
		loadFrom(new EnhancedJsonObject());
	}
	
	public void load(File preferencesFile) throws Exception
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
	
	public void setMainWindowHeigth(int mainWindowHeightToUse)
	{
		mainWindowHeight = mainWindowHeightToUse;
	}
	
	public int getMainWindowHeight()
	{
		return mainWindowHeight;
	}
	
	public void setMainWindowWidth(int mainwWindowWidthToUse)
	{
		mainWindowWidth = mainwWindowWidthToUse;
	}
	
	public void setMainWindowXPosition(int xPosition)
	{
		mainWindowXPosition = xPosition;
	}
	
	public int getMainWindowXPosition()
	{
		return mainWindowXPosition;
	}
	
	public void setMainWindowYPosition(int yPosition)
	{
		mainWindowYPosition = yPosition;
	}
	
	public int getMainWindowYPosition()
	{
		return mainWindowYPosition;
	}

	
	public int getMainWindowWidth()
	{
		return mainWindowWidth;
	}
	
	public void setLanguageCode(String newLanguageCode)
	{
		if(newLanguageCode == null)
			newLanguageCode = DEFAULT_LANGUAGE_CODE;
		languageCode = newLanguageCode;
	}
	
	public String getLanguageCode()
	{
		return languageCode;
	}
	
	public void setNewsText(String newsTextToUse)
	{
		newsText = newsTextToUse;
	}
	
	public String getNewsText()
	{
		return newsText;
	}
	
	public void setNewsDate(String newsDateToUse)
	{
		newsDate = newsDateToUse;
	}
	
	public String getNewsDate()
	{
		return newsDate;
	}
	
	public void setInstalledSampleVersion(CodeList installedSampleVersionsToUse)
	{
		installedSampleVersions = installedSampleVersionsToUse;
	}
	
	public CodeList getInstalledSampleVersions()
	{
		return new CodeList(installedSampleVersions);
	}
	
	public boolean getIsCellRatingsVisible()
	{
		return getBoolean(AppPreferences.TAG_CELL_RATINGS_VISIBLE);
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
		if(tag.equals(TAG_COLOR_SCOPE_BOX))
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
		else if(tag.equals(TAG_COLOR_SCOPE_BOX))
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
		if(tag == null)
			EAM.logError("AppPreferences setTaggedInt null key value = " + value);
		taggedIntMap.put(tag, new Integer(value));
	}
	
	public int getTaggedInt(String tag)
	{
		Integer value = taggedIntMap.get(tag);
		if(value == null)
			return 0;
		return value.intValue();
	}
	
	public void setTaggedString(String tag, String value)
	{
		taggedStringMap.put(tag, new String(value));
	}	
	
	public String getTaggedString(String tag)
	{
		String value = taggedStringMap.get(tag);
		if(value == null)
			return "";
		return value;
	}
	
	public int getPanelFontSize()
	{
		return panelFontSize;
	}
	
	public void setPanelFontSize(int fontSize)
	{
		panelFontSize = fontSize;
	}
	
	public String getPanelFontFamily()
	{
		return panelFontFamily;
	}
	
	public void setPanelFontFamily(String fontFamily)
	{
		panelFontFamily = fontFamily;
	}
	
	public int getWizardFontSize()
	{
		return panelFontSize;
	}
	
	public void setWizardFontSize(int fontSize)
	{
		wizardFontSize = fontSize;
	}
	
	public String getWizardFontFamily()
	{
		return panelFontFamily;
	}
	
	public void setWizardFontFamily(String fontFamily)
	{
		wizardFontFamily = fontFamily;
	}
	
	public void setRowHeightMode(String rowHeightMode)
	{
		rowHeightModeCode = rowHeightMode;
	}

	public String getRowHeightMode()
	{
		return rowHeightModeCode;
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
		json.put(TAG_COLOR_SCOPE_BOX, scopeColor);
		json.put(TAG_COLOR_INTERMEDIATE_RESULT, intermediateResultColor);
		json.put(TAG_COLOR_THREAT_REDUCTION_RESULT, threatReductionResultColor);
		json.put(TAG_IS_MAXIMIZED, isMaximized);
		json.put(TAG_MAIN_WINDOW_HEIGHT, mainWindowHeight);
		json.put(TAG_MAIN_WINDOW_WIDTH, mainWindowWidth);
		json.put(TAG_MAIN_WINDOW_X_POSITION, mainWindowXPosition);
		json.put(TAG_MAIN_WINDOW_Y_POSITION, mainWindowYPosition);
		json.put(TAG_GRID_VISIBLE, isGridVisible);
		json.put(TAG_CELL_RATINGS_VISIBLE, isCellRatingsVisible);
		json.put(TAG_LANGUAGE_CODE, languageCode);
		json.put(TAG_NEWS_TEXT, newsText);
		json.put(TAG_NEWS_DATE, newsDate);
		json.put(TAG_INSTALLED_SAMPLE_VERSIONS, installedSampleVersions.toString());
		
		json.put(TAG_WIZARD_FONT_FAMILY, wizardFontFamily);
		json.put(TAG_WIZARD_FONT_SIZE, Integer.toString(wizardFontSize));
		
		json.put(TAG_PANEL_FONT_FAMILY, panelFontFamily);
		json.put(TAG_PANEL_FONT_SIZE, Integer.toString(panelFontSize));
		
		json.put(TAG_ROW_HEIGHT_MODE, rowHeightModeCode);
		
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
			String value = taggedStringMap.get(key);
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
			Integer value = taggedIntMap.get(key);
			taggedIntJson.put(key, value.intValue());
		}
		return taggedIntJson;
	}
	
	public void loadFrom(EnhancedJsonObject json) throws Exception
	{
		strategyColor = json.optColor(TAG_COLOR_STRATEGY, DiagramConstants.DEFAULT_STRATEGY_COLOR);
		activitiesColor = json.optColor(TAG_COLOR_ACTIVITIES, DiagramConstants.DEFAULT_ACTIVITIES_COLOR);
		contributingFactorColor = json.optColor(TAG_COLOR_CONTRIBUTING_FACTOR, DiagramConstants.DEFAULT_CONTRIBUTING_FACTOR_COLOR);
		directThreatColor = json.optColor(TAG_COLOR_DIRECT_THREAT, DiagramConstants.DEFAULT_DIRECT_THREAT_COLOR);
		targetColor = json.optColor(TAG_COLOR_TARGET, DiagramConstants.DEFAULT_TARGET_COLOR);
		scopeColor = json.optColor(TAG_COLOR_SCOPE_BOX, DiagramConstants.DEFAULT_SCOPE_COLOR);
		intermediateResultColor = json.optColor(TAG_COLOR_INTERMEDIATE_RESULT, DiagramConstants.DEFAULT_INTERMEDIATE_RESULT_COLOR);
		threatReductionResultColor = json.optColor(TAG_COLOR_THREAT_REDUCTION_RESULT, DiagramConstants.DEFAULT_THREAT_REDUCTION_RESULT_COLOR);
		
		isGridVisible = json.optBoolean(TAG_GRID_VISIBLE, DEFAULT_GRID_VISIBILITY_VALUE);
		isMaximized = json.optBoolean(TAG_IS_MAXIMIZED, DEFAULT_IS_MAXIMIZED_VALUE);
		mainWindowHeight = json.optInt(TAG_MAIN_WINDOW_HEIGHT, DEFAULT_MAIN_WINDOW_HEIGHT);
		mainWindowWidth = json.optInt(TAG_MAIN_WINDOW_WIDTH, DEFAULT_MAIN_WINDOW_WIDTH);
		mainWindowXPosition = json.optInt(TAG_MAIN_WINDOW_X_POSITION, DEFAULT_MAIN_WINDOW_X_POSITION);
		mainWindowYPosition = json.optInt(TAG_MAIN_WINDOW_Y_POSITION, DEFAULT_MAIN_WINDOW_Y_POSITION);
		isCellRatingsVisible = json.optBoolean(TAG_CELL_RATINGS_VISIBLE, DEFAULT_IS_CELL_RATING_VISIBLE);
		languageCode = json.optString(TAG_LANGUAGE_CODE, DEFAULT_LANGUAGE_CODE);
		newsText = json.optString(TAG_NEWS_TEXT);
		newsDate = json.optString(TAG_NEWS_DATE);
		installedSampleVersions = new CodeList(json.optString(TAG_INSTALLED_SAMPLE_VERSIONS));
		
		wizardFontFamily = json.optString(TAG_WIZARD_FONT_FAMILY);
		wizardFontSize = json.optInt(TAG_WIZARD_FONT_SIZE);
		
		panelFontFamily = json.optString(TAG_PANEL_FONT_FAMILY);
		panelFontSize = json.optInt(TAG_PANEL_FONT_SIZE);
		
		rowHeightModeCode = json.optString(TAG_ROW_HEIGHT_MODE);
		
		taggedIntMap = loadTagIntegerMap(json);
		taggedStringMap = loadTagStringMap(json);
	}

	private HashMap loadTagStringMap(EnhancedJsonObject json)
	{
		HashMap map = new HashMap<String, String>();
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
		HashMap map = new HashMap<String, Integer>();
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
	
	public static String convertToHexString(Color color)
	{
		String rgb = Integer.toHexString(color.getRGB());
		return rgb.substring(2, rgb.length());
	}
		
	public void setWizardTitleBackground(String colorToUse)
	{
		wizardTitleBackgroundColor = colorToUse;
	}
	public static Color getWizardTitleBackground()
	{
		return Color.decode(getWizardTitleBackgroundColorForCss());
	}
	public static String getWizardTitleBackgroundColorForCss()
	{
		return wizardTitleBackgroundColor;
	}	

	public void setWizardBackgroundColor(String colorToUse)
	{
		wizardBackgroundColor = colorToUse;
	}
	public static Color getWizardBackgroundColor()
	{
		return Color.decode(getWizardBackgroundColorForCss());
	}
	public static String getWizardBackgroundColorForCss()
	{
		return wizardBackgroundColor;
	}
	
	public void setWizardSidebarBackgroundColor(String colorToUse)
	{
		wizardSidebarBackgroundColor = colorToUse;
	}

	public static Color getSideBarBackgroundColor()
	{
		return Color.decode(wizardSidebarBackgroundColor);
	}
	
	public static String getWizardSidebarBackgroundColorForCss()
	{
		return wizardSidebarBackgroundColor;
	}
			
	public void setDarkControlPanelBackgroundColor(String colorToUse)
	{
		darkPanelBackgroundColor = Color.decode(colorToUse);
	}
	public static Color getDarkPanelBackgroundColor()
	{
		return darkPanelBackgroundColor;
	}
	
	public void setDataPanelBackgroundColor(String colorToUse)
	{
		dataPanelBackgroundColorForCss = colorToUse;
	}

	public static Color getDataPanelBackgroundColor()
	{
		return Color.decode(dataPanelBackgroundColorForCss);
	}

	public static String getDataPanelBackgroundColorForCss()
	{
		return dataPanelBackgroundColorForCss;
	}

	public void setControlPanelBackgroundColor(String colorToUse)
	{
		controlPanelBackgroundColor = Color.decode(colorToUse);
	}
	public static Color getControlPanelBackgroundColor()
	{
		return controlPanelBackgroundColor;
	}
	
	public static Color getWorkUnitsBackgroundColor()
	{
		return WORK_UNITS_TOTAL_BACKGROUND;
	}
	
	public static Color getWorkUnitsBackgroundColor(DateUnit dateUnit)
	{
		if(dateUnit.isBlank())
			return getWorkUnitsBackgroundColor();
		if(dateUnit.isYear())
			return WORK_UNITS_YEAR_BACKGROUND;
		if(dateUnit.isQuarter())
			return WORK_UNITS_QUARTER_BACKGROUND;
		if(dateUnit.isMonth())
			return WORK_UNITS_MONTH_BACKGROUND;
		if(dateUnit.isDay())
			return WORK_UNITS_DAY_BACKGROUND;
		EAM.logWarning("Unknown work units date unit type: " + dateUnit);
		return WORK_UNITS_TOTAL_BACKGROUND;
	}

	public static final String TAG_COLOR_STRATEGY = "ColorIntervention";
	public static final String TAG_COLOR_ACTIVITIES = "ColorActivities";
	public static final String TAG_COLOR_CONTRIBUTING_FACTOR = "ColorIndirectFactor";
	public static final String TAG_COLOR_DIRECT_THREAT = "ColorDirectThreat";
	public static final String TAG_COLOR_TARGET = "ColorTarget";
	public static final String TAG_COLOR_SCOPE_BOX = "ColorScope";
	public static final String TAG_COLOR_INTERMEDIATE_RESULT = "ColorIntermediateResult";
	public static final String TAG_COLOR_THREAT_REDUCTION_RESULT = "ColorThreatReductionResult";
	public static final String TAG_IS_MAXIMIZED = "IsMaximized";
	public static final String TAG_MAIN_WINDOW_HEIGHT = "MainWindowHeight";
	public static final String TAG_MAIN_WINDOW_WIDTH = "MainWindowWidth";
	public static final String TAG_MAIN_WINDOW_X_POSITION = "MainwWindowX";
	public static final String TAG_MAIN_WINDOW_Y_POSITION = "MainwWindowY";
	public static final String TAG_NEWS_TEXT = "NewsText";
	public static final String TAG_NEWS_DATE = "NewsDate";
	public static final String TAG_INSTALLED_SAMPLE_VERSIONS = "InstalledSampleVersions";
	
	public static final String TAG_GRID_VISIBLE = "GridVisible";
	public static final String TAG_CELL_RATINGS_VISIBLE = "CellRatingsVisible";
	public static final String TAG_LANGUAGE_CODE = "LanguageCode";
	public static final String TAG_TAGGED_INTS = "TaggedInts";
	public static final String TAG_TAGGED_STRINGS = "TaggedStrings";
	public static final String TAG_DIAGRAM_ZOOM = "DiagramZoom";
	public static final String TAG_PLANNING_VIEW_FIRST_TIME_USE_BOOLEAN = "PlanningViewFirstTimeUse";
	
	public static final String TAG_WIZARD_FONT_FAMILY = "WizardFontFamily";
	public static final String TAG_WIZARD_FONT_SIZE = "WizardFontSize";
	
	public static final String TAG_PANEL_FONT_FAMILY = "PanelFontFamily";
	public static final String TAG_PANEL_FONT_SIZE = "PanelFontSize";
	
	public static final String TAG_ROW_HEIGHT_MODE = "TableRowHeightMode";
	
	public static final Color WIZARD_TITLE_FOREGROUND = new Color(72, 112, 28);
	public static final String WIZARD_TITLE_FOREGROUND_FOR_CSS = "#48701C";

	private static String wizardTitleBackgroundColor = "#A2D760";
	private static String wizardBackgroundColor = "#D7EEBB";
	private static String wizardSidebarBackgroundColor = "#E2f3cc";
	
	private static Color darkPanelBackgroundColor =  new Color(0x57, 0xAC, 0xD5);
	private static String dataPanelBackgroundColorForCss = "#DAEDF5";
	private static Color controlPanelBackgroundColor = new Color(0xE8, 0xDA, 0x97);
		

	private static final Color LIGHT_PURPLE = new Color(204,153,255);
	public static final Color INDICATOR_COLOR = LIGHT_PURPLE;
	
	public static final Color RESOURCE_TABLE_BACKGROUND = new Color(0x99, 0xcc, 0xff);
	public static final Color BUDGET_TABLE_BACKGROUND = new Color(0xcc, 0xff, 0xcc);
	public static final Color BUDGET_TOTAL_TABLE_BACKGROUND = new Color(0x99, 0xee, 0x99);
	public static final Color MEASUREMENT_COLOR_BACKGROUND = new Color(0xff, 0xf0, 0xb6);

	private static final Color WORK_UNITS_TOTAL_BACKGROUND = new Color(0xff, 0xCC, 0x55);
	private static final Color WORK_UNITS_YEAR_BACKGROUND = new Color(0xff, 0xDD, 0x66);
	private static final Color WORK_UNITS_QUARTER_BACKGROUND = new Color(0xff, 0xEE, 0x77);
	private static final Color WORK_UNITS_MONTH_BACKGROUND = new Color(0xff, 0xFF, 0x88);
	private static final Color WORK_UNITS_DAY_BACKGROUND = new Color(0xff, 0xFF, 0xAA);

	private static final boolean DEFAULT_GRID_VISIBILITY_VALUE = false;
	private static final boolean DEFAULT_IS_MAXIMIZED_VALUE = false;
	private static final boolean DEFAULT_IS_CELL_RATING_VISIBLE = false;
	private static final String DEFAULT_LANGUAGE_CODE = "en";
	private static final int DEFAULT_MAIN_WINDOW_HEIGHT = 600;
	private static final int DEFAULT_MAIN_WINDOW_WIDTH = 800;

	private static final int DEFAULT_MAIN_WINDOW_X_POSITION = 100;
	private static final int DEFAULT_MAIN_WINDOW_Y_POSITION = 100;

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
	private int mainWindowHeight;
	private int mainWindowWidth;
	private int mainWindowXPosition;
	private int mainWindowYPosition;
	private String languageCode;
	private String newsText;
	private String newsDate;
	private CodeList installedSampleVersions;
	
	private double diagramZoomSetting;
	
	private String wizardFontFamily;
	private int wizardFontSize;
	
	private String panelFontFamily;
	private int panelFontSize;

	private String rowHeightModeCode;

	private HashMap<String, Integer> taggedIntMap;
	private HashMap<String, String> taggedStringMap;
}
