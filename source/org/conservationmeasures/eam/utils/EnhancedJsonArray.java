/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnhancedJsonArray extends JSONArray
{
	public EnhancedJsonArray()
	{
		
	}
	
	public EnhancedJsonArray(JSONArray copyFrom)
	{
		for(int i = 0; i < copyFrom.length(); ++i)
			put(i, copyFrom.get(i));
	}

	public JSONObject getJSONObject(int index) throws NoSuchElementException
	{
		throw new RuntimeException("Use getJson instead!");
	}

	public EnhancedJsonObject getJson(int index) throws NoSuchElementException
	{
		return new EnhancedJsonObject(super.getJSONObject(index));
	}
}
