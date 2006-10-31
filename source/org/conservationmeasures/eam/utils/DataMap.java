/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import java.text.ParseException;

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
	
	public void putInt(String tag, int value)
	{
		put(tag, new Integer(value));
	}
	
	public void putString(String tag, String value)
	{
		put(tag, value);
	}
	
	public int getInt(String tag)
	{
		return ((Integer)get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)get(tag);
	}
}
