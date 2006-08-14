/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnhancedJsonObject extends JSONObject
{
	public EnhancedJsonObject()
	{
		super();
	}
	
	public EnhancedJsonObject(String jsonString) throws ParseException
	{
		super(jsonString);
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
}
