/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

public class CodeList
{
	public CodeList()
	{
		this(new Vector());
	}
	
	public CodeList(CodeList copyFrom)
	{
		this(new Vector(copyFrom.data));
	}
	
	
	public CodeList(EnhancedJsonObject json)
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_IDS);
		if(array == null)
			array = new EnhancedJsonArray();
		for(int i = 0; i < array.length(); ++i)
			add((String)array.get(i));
		
	}
	
	public CodeList(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	private CodeList(List dataToUse)
	{
		data = new Vector(dataToUse);
	}
	
	public int size()
	{
		return data.size();
	}

	
	public void add(String id)
	{
		data.add(id);
	}
		
	public String get(int index)
	{
		return (String)data.get(index);
	}
	
	public boolean contains(String id)
	{
		return data.contains(id);
	}
	
	public int find(String id)
	{
		return data.indexOf(id);
	}
	
	public void removeId(String id)
	{
		if(!data.contains(id))
			throw new RuntimeException("Attempted to remove non-existant Id: " + id + " from: " + toString());
		data.remove(id);
	}
	
	public void subtract(CodeList other)
	{
		for(int i = 0; i < other.size(); ++i)
		{
			String id = other.get(i);
			if(contains(id))
				removeId(id);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonArray array = new EnhancedJsonArray();
		for(int i = 0; i < size(); ++i)
			array.appendString(get(i));
		json.put(TAG_IDS, array);
		return json;
	}
	
	public String toString()
	{
		if(size() == 0)
			return "";
		return toJson().toString();
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof CodeList))
			return false;
		
		CodeList other = (CodeList)rawOther;
		return data.equals(other.data);
	}
	
	public int hashCode()
	{
		return data.hashCode();
	}
		
	private static final String TAG_IDS = "Codes";

	Vector data;

}

