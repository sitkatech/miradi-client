/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.json.JSONObject;

public class DataMap extends EnhancedJsonObject
{
	public DataMap()
	{
	}
	
	public DataMap(JSONObject copyFrom) throws ParseException
	{
		super(copyFrom.toString());
	}
	
	public void putId(String tag, BaseId id)
	{
		putInt(tag, id.asInt());
	}
	
	public void putInt(String tag, int value)
	{
		put(tag, new Integer(value));
	}
	
	public void putString(String tag, String value)
	{
		put(tag, value);
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
	
	public BaseId getId(String tag)
	{
		return new BaseId(getInt(tag));
	}
	
	public int getInt(String tag)
	{
		return ((Integer)get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)get(tag);
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

	private static String TAG_POINT_X = "X";
	private static String TAG_POINT_Y = "Y";
	private static String TAG_WIDTH = "Width";
	private static String TAG_HEIGHT = "Height";
}
