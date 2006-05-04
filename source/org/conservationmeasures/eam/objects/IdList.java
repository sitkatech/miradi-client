/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class IdList
{
	public IdList()
	{
		this(new Vector());
	}
	
	public IdList(IdList copyFrom)
	{
		this(new Vector(copyFrom.data));
	}
	
	public IdList(JSONObject json)
	{
		this();
		JSONArray array = json.optJSONArray(TAG_IDS);
		if(array == null)
			array = new JSONArray();
		for(int i = 0; i < array.length(); ++i)
			add(array.getInt(i));
		
	}
	
	public IdList(String listAsJsonString) throws ParseException
	{
		this(new JSONObject(listAsJsonString));
	}
	
	private IdList(Vector dataToUse)
	{
		data = dataToUse;
	}
	
	public int size()
	{
		return data.size();
	}
	
	public void add(int id)
	{
		data.add(new Integer(id));
	}
	
	public void insertAt(int id, int at)
	{
		data.insertElementAt(new Integer(id), at);
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
	
	public String toString()
	{
		return toJson().toString();
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
	
	public IdList createClone()
	{
		return new IdList(this);
	}
	
	
	private static final String TAG_IDS = "Ids";

	Vector data;

}
