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
package org.miradi.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;

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
		String pointAsString = convertFromPoint(value);
		put(tag, pointAsString);
	}

	public static String convertFromPoint(Point point)
	{
		JSONObject json = new JSONObject();
		json.put(TAG_POINT_X, point.x);
		json.put(TAG_POINT_Y, point.y);
		
		return json.toString();
	}
	
	public static Point convertToPoint(String pointAsString) throws ParseException
	{
		JSONObject point = new EnhancedJsonObject(pointAsString);
		return new Point(point.getInt(TAG_POINT_X), point.getInt(TAG_POINT_Y));
	}
	
	public static Dimension convertToDimension(String dimensionAsString) throws ParseException
	{
		JSONObject dimension = new EnhancedJsonObject(dimensionAsString);
		return new Dimension(dimension.getInt(TAG_WIDTH), dimension.getInt(TAG_HEIGHT));
	}
	
	public static String convertFromDimension(Dimension dimension)
	{
		JSONObject json = new JSONObject();
		json.put(TAG_WIDTH, dimension.width);
		json.put(TAG_HEIGHT, dimension.height);
		
		return json.toString();
	}

	public void putDimension(String tag, Dimension value)
	{
		JSONObject size = new JSONObject();
		size.put(TAG_WIDTH, value.width);
		size.put(TAG_HEIGHT, value.height);
		put(tag, size);
	}

	public Point getPoint(String tag) throws Exception
	{
		String pointAsString = getString(tag);
		return convertToPoint(pointAsString);
	}

	public Dimension getDimension(String tag) throws ParseException
	{
		String dimensionAsString = getString(tag);
		return convertToDimension(dimensionAsString);
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
	
	public void putRef(String tag, ORef ref)
	{
		put(tag, ref.toString());
	}
	
	public void putId(String tag, BaseId id)
	{
		put(tag, id.asInt());
	}

	public BaseId getId(String tag)
	{
		return new BaseId(getInt(tag));
	}
	
	public ORef getRef(String tag)
	{
		return ORef.createFromString(getString(tag));
	}
	
	public BaseId optId(String tag)
	{
		if(has(tag))
			return getId(tag);
		return BaseId.INVALID;
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof EnhancedJsonObject))
			return false;
		
		EnhancedJsonObject other = (EnhancedJsonObject)rawOther;
		
		// NOTE: we would love to access the hashmap directly, 
		// but they used default permissions instead of protected
		
		Iterator iter = keys();
		if(length() != other.length())
			return false;
		
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			if(!other.has(key))
				return false;
			Object thisValue = get(key);
			Object otherValue = other.get(key);
			if(thisValue.equals(otherValue))
				continue;
			
			if(thisValue instanceof String)
			{
				try
				{
					EnhancedJsonObject thisJson = new EnhancedJsonObject((String)thisValue);
					EnhancedJsonObject otherJson = new EnhancedJsonObject((String)otherValue);
					if(!thisJson.equals(otherJson))
						return false;
				}
				catch(Exception e)
				{
					return false;
				}
			}
		}
		
		return true;
	}

	//NOTE: could be improved at some point, but this works
	public int hashCode()
	{
		return length();
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
