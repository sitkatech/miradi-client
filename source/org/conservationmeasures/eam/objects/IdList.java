/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;
import java.util.List;
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
	
	public IdList(int[] ids)
	{
		this();
		for(int i = 0; i < ids.length; ++i)
			add(ids[i]);
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
	
	private IdList(List dataToUse)
	{
		data = new Vector(dataToUse);
	}
	
	public int size()
	{
		return data.size();
	}
	
	public void clear()
	{
		data.clear();
	}
	
	public boolean isEmpty()
	{
		return (size() == 0);
	}
	
	public void add(int id)
	{
		data.add(new Integer(id));
	}
	
	public void addAll(IdList otherList)
	{
		for(int i = 0; i < otherList.size(); ++i)
			add(otherList.get(i));
	}
	
	public void insertAt(int id, int at)
	{
		data.insertElementAt(new Integer(id), at);
	}
	
	public int get(int index)
	{
		return ((Integer)data.get(index)).intValue();
	}
	
	public boolean contains(int id)
	{
		return data.contains(new Integer(id));
	}
	
	public void removeId(int id)
	{
		Integer idObject = new Integer(id);
		if(!data.contains(idObject))
			throw new RuntimeException("Attempted to remove non-existant Id: " + id + " from: " + toString());
		data.remove(idObject);
	}
	
	public void subtract(IdList other)
	{
		for(int i = 0; i < other.size(); ++i)
		{
			int id = other.get(i);
			if(contains(id))
				removeId(id);
		}
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
