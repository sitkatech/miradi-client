/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.json.JSONObject;

public class DataMap 
{
	public DataMap()
	{
		data = new JSONObject();
	}
	
	public void putInt(String tag, int value)
	{
		data.put(tag, new Integer(value));
	}
	
	public void putString(String tag, String value)
	{
		data.put(tag, value);
	}
	
	public void putPoint(String tag, Point value)
	{
		JSONObject point = new JSONObject();
		point.put(POINT_X, value.x);
		point.put(POINT_Y, value.y);
		data.put(tag, point);
	}
	
	public int getInt(String tag)
	{
		return ((Integer)data.get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)data.get(tag);
	}
	
	public Point getPoint(String tag)
	{
		JSONObject point = data.getJSONObject(tag);
		return new Point(point.getInt(POINT_X), point.getInt(POINT_Y));
	}

	private static String POINT_X = "X";
	private static String POINT_Y = "Y";

	private JSONObject data;
}
