/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class IdList
{
	public IdList()
	{
		data = new Vector();
	}
	
	public IdList(JSONObject json)
	{
		this();
		JSONArray array = json.getJSONArray(TAG_IDS);
		for(int i = 0; i < array.length(); ++i)
			add(array.getInt(i));
		
	}
	
	public int size()
	{
		return data.size();
	}
	
	public void add(int id)
	{
		data.add(new Integer(id));
	}
	
	public int get(int index)
	{
		return ((Integer)data.get(index)).intValue();
	}
	
	public void removeId(int id)
	{
		Integer idObject = new Integer(id);
		if(!data.contains(idObject))
			throw new RuntimeException("Attempted to remove non-existant Id");
		data.remove(idObject);
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for(int i = 0; i < size(); ++i)
			array.appendInt(get(i));
		json.put(TAG_IDS, array);
		return json;
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof IdList))
			return false;
		
		IdList other = (IdList)rawOther;
		return data.equals(other.data);
	}
	
	public int hashCode()
	{
		return data.hashCode();
	}
	
	
	private static final String TAG_IDS = "Ids";

	Vector data;

}
