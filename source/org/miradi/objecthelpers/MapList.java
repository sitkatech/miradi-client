/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.text.ParseException;

import org.miradi.utils.EnhancedJsonObject;

public class MapList
{
	public MapList() throws ParseException
	{
		this("");
	}
	
	public MapList(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	private MapList(EnhancedJsonObject dataToUse)
	{
		data = new EnhancedJsonObject(dataToUse);
	}
	
	public int size()
	{
		return data.length();
	}
	
	public void clear()
	{
		data.removeAll();
	}
	
	public boolean isEmpty()
	{
		return (size() == 0);
	}
	
	public void put(String key, String object)
	{
		data.put(key, object);
	}
	
	public String get(String key)
	{
		return data.getString(key);
	}
	
	public boolean contains(String key)
	{
		return data.has(key);
	}
	
	public void remove(String key)
	{
		if(!data.has(key))
			throw new RuntimeException("Attempted to remove non-existant key: " + key + " from: " + toString());
		data.remove(key);
	}

	public String toString()
	{
		return data.toString();
	}
	
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof MapList))
			return false;
		
		MapList other = (MapList)rawOther;
		return data.equals(other.data);
	}

	public int hashCode()
	{
		return data.hashCode();
	}

	EnhancedJsonObject data;
}

