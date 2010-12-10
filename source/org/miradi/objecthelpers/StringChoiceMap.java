/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.miradi.utils.EnhancedJsonObject;

public class StringChoiceMap
{
	public StringChoiceMap()
	{
		this(new HashMap<String, String>());
	}

	public StringChoiceMap(StringChoiceMap copyFrom)
	{
		this(new HashMap<String, String>(copyFrom.data));
	}

	public StringChoiceMap(EnhancedJsonObject json)
	{
		this();
		copyFromJson(json);
	}
	
	public StringChoiceMap(String mapAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(mapAsJsonString));
	}
	
	private StringChoiceMap(Map<String, String> dataToUse)
	{
		data = new HashMap<String, String>(dataToUse);
	}

	private void copyFromJson(EnhancedJsonObject json)
	{
		data.clear();
		EnhancedJsonObject array = json.optJson(TAG_STRING_CHOICE_MAP);
		if(array == null)
			array = new EnhancedJsonObject();
		Iterator iterator = array.keys();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			String code = array.getString(key);
			add(key, code);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonObject array = new EnhancedJsonObject();
		
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			array.put(key, get(key).toString());
		}
		json.put(TAG_STRING_CHOICE_MAP, array);

		return json;
	}

	public int size()
	{
		return data.size();
	}

	public void add(String code, String object)
	{
		data.put(code, object);
	}

	public HashMap<String, String> toHashMap()
	{
		return data;
	}
	
	public String get(String code)
	{
		String value = data.get(code);
		if (value == null)
			return null;
		
		return value;
	}

	public String get()
	{
		if(size() == 0)
			return "";
		
		return toJson().toString();
	}

	public void set(String newValue) throws Exception
	{
		copyFromJson(new EnhancedJsonObject(newValue));
	}

	public String find(String object)
	{
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
			if (object.equals(data.get(key)))
				return key;
		}
		
		return null;
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringChoiceMap))
			return false;

		StringChoiceMap other = (StringChoiceMap) rawOther;
		return data.equals(other.data);
	}

	@Override
	public int hashCode()
	{
		return data.hashCode();
	}
	
	@Override
	public String toString()
	{
		if(size() == 0)
			return "";

		return toJson().toString();
	}

	private static final String TAG_STRING_CHOICE_MAP = "StringChoiceMap";

	private HashMap<String, String> data;
}
