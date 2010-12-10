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

public class StringChoiceMap extends AbstractStringMap
{
	public StringChoiceMap()
	{
		this(new HashMap<String, String>());
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
		EnhancedJsonObject array = json.optJson(getMapTag());
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

	private String getMapTag()
	{
		return TAG_STRING_CHOICE_MAP;
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
		json.put(getMapTag(), array);

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
