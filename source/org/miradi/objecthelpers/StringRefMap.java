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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.miradi.utils.EnhancedJsonObject;

public class StringRefMap
{
	public StringRefMap()
	{
		this(new HashMap<String, ORef>());
	}

	public StringRefMap(StringRefMap copyFrom)
	{
		this(new HashMap<String, ORef>(copyFrom.data));
	}

	public StringRefMap(EnhancedJsonObject json)
	{
		this();
		copyFromJson(json);
	}
	
	public StringRefMap(String mapAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(mapAsJsonString));
	}
	
	private StringRefMap(HashMap<String, ORef> dataToUse)
	{
		data = new HashMap<String, ORef>(dataToUse);
	}

	private void copyFromJson(EnhancedJsonObject json)
	{
		data.clear();
		EnhancedJsonObject jsonArray = json.optJson(TAG_STRING_REF_MAP);
		if(jsonArray == null)
			jsonArray = new EnhancedJsonObject();
		
		Iterator iterator = jsonArray.keys();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			ORef refValue = jsonArray.getRef(key);
			add(key, refValue);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonObject jsonArray = new EnhancedJsonObject();
		
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			ORef refValue = getValue(key);
			jsonArray.put(key, refValue.toString());
		}
		
		json.put(TAG_STRING_REF_MAP, jsonArray);
		return json;
	}

	public int size()
	{
		return data.size();
	}

	public void add(String key, ORef value)
	{
		data.put(key, value);
	}

	public HashMap toHashMap()
	{
		return data;
	}
	
	public ORef getValue(String key)
	{
		ORef value = data.get(key);
		if (value == null)
			return ORef.INVALID;
		
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

	public String findKey(ORef value)
	{
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			if (value.equals(data.get(key)))
				return key;
		}
		
		return "";
	}
	
	public Set getKeys()
	{
		return data.keySet();
	}
	
	public Collection<ORef> getValues()
	{
		return data.values();
	}

	public boolean containsValue(ORef value)
	{
		return data.containsValue(value);
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringRefMap))
			return false;

		StringRefMap other = (StringRefMap) rawOther;
		return data.equals(other.data);
	}

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

	private static final String TAG_STRING_REF_MAP = "StringRefMap";

	private HashMap<String, ORef> data;
}
