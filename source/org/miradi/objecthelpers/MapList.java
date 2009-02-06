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

