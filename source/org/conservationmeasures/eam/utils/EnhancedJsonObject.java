/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnhancedJsonObject extends JSONObject
{
	public EnhancedJsonObject()
	{
		super();
	}
	
	public EnhancedJsonObject(JSONObject json)
	{
		fillFrom(json);
	}

	private void fillFrom(JSONObject json)
	{
		removeAll();
		Iterator iter = json.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			put(key, json.get(key));
		}
	}
	
	public EnhancedJsonObject(String jsonString) throws ParseException
	{
		super(handleEmptyString(jsonString));
	}

	// Override to return empty array instead of null if missing
	public JSONArray optJSONArray(String key)
	{
		JSONArray array = super.optJSONArray(key);
		if(array == null)
			array = new JSONArray();
		
		return array;
	}

	// Don't use this!
	public JSONObject getJSONObject(String key)
	{
		throw new RuntimeException("Use getJson instead!");
	}
	
	// Don't use this!
	public JSONObject optJSONObject(String key)
	{
		throw new RuntimeException("Use optJson instead!");
	}
	
	public EnhancedJsonObject getJson(String key)
	{
		return new EnhancedJsonObject(super.getJSONObject(key));
	}
	
	// Override to return empty object instead of null if missing
	public EnhancedJsonObject optJson(String key)
	{
		if(!has(key))
			return new EnhancedJsonObject();
		
		return getJson(key);
	}
	
	public void put(String tag, Color color)
	{
		JSONArray rgb = new JSONArray();
		rgb.appendInt(color.getRed());
		rgb.appendInt(color.getGreen());
		rgb.appendInt(color.getBlue());
		put(tag, rgb);
	}

	public Color getColor(String tag)
	{
		JSONArray rgb = getJSONArray(tag);
		int red = rgb.getInt(0);
		int green = rgb.getInt(1);
		int blue = rgb.getInt(2);
		return new Color(red, green, blue);
	}
	
	public Color optColor(String tag, Color defaultColor)
	{
		if(!has(tag))
			return defaultColor;
		return getColor(tag);
	}
	
	public void removeAll()
	{
		Iterator iter = keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			remove(key);
		}

	}
	
	private static String handleEmptyString(String possiblyEmptyJsonString)
	{
		if(possiblyEmptyJsonString.length() == 0)
			return "{}";
		return possiblyEmptyJsonString;
	}

}
