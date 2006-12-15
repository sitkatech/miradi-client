/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.text.ParseException;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
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

	// Override to return empty array instead of null if missing
	public JSONArray optJSONArray(String key)
	{
		throw new RuntimeException("Use optJsonArray instead!");
	}
	
	public EnhancedJsonArray optJsonArray(String key)
	{
		if (has(key))
			return getJsonArray(key);
		
		return new EnhancedJsonArray();
	}
	
	public JSONArray getJSONArray(String key)
	{
		throw new RuntimeException("Use getJsonArray instead!");
	}
	
	public EnhancedJsonArray getJsonArray(String key)
	{
		return new EnhancedJsonArray(super.getJSONArray(key));
	}
	
	public JSONObject put(String tag, Object value)
	{
		if(value instanceof String || 
				value instanceof JSONObject ||
				value instanceof JSONArray ||
				value instanceof Integer ||
				value instanceof Boolean ||
				value instanceof Double)
			return super.put(tag, value);
		
		throw new RuntimeException("This cannot be used for generic Objects: " + value.getClass());
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
		JSONArray rgb = getJsonArray(tag);
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
	
	public void putPoint(String tag, Point value)
	{
		JSONObject point = new JSONObject();
		point.put(TAG_POINT_X, value.x);
		point.put(TAG_POINT_Y, value.y);
		put(tag, point);
	}

	public void putDimension(String tag, Dimension value)
	{
		JSONObject size = new JSONObject();
		size.put(TAG_WIDTH, value.width);
		size.put(TAG_HEIGHT, value.height);
		put(tag, size);
	}

	public Point getPoint(String tag)
	{
		JSONObject point = getJson(tag);
		return new Point(point.getInt(TAG_POINT_X), point.getInt(TAG_POINT_Y));
	}

	public Dimension getDimension(String tag)
	{
		JSONObject size = getJson(tag);
		return new Dimension(size.getInt(TAG_WIDTH), size.getInt(TAG_HEIGHT));
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
	
	public void putId(String tag, BaseId id)
	{
		put(tag, id.asInt());
	}

	public BaseId getId(String tag)
	{
		return new BaseId(getInt(tag));
	}
	
	public BaseId optId(String tag)
	{
		if(has(tag))
			return getId(tag);
		return BaseId.INVALID;
	}

	private static String handleEmptyString(String possiblyEmptyJsonString)
	{
		if(possiblyEmptyJsonString.length() == 0)
			return "{}";
		return possiblyEmptyJsonString;
	}

	private static String TAG_POINT_X = "X";
	private static String TAG_POINT_Y = "Y";
	private static String TAG_WIDTH = "Width";
	private static String TAG_HEIGHT = "Height";
	
}
