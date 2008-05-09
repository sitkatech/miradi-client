/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.utils.EnhancedJsonObject;

public class StringRefMap
{
	public StringRefMap()
	{
		this(new HashMap<ORef, String>());
	}

	public StringRefMap(StringRefMap copyFrom)
	{
		this(new HashMap<ORef, String>(copyFrom.data));
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
	
	private StringRefMap(HashMap<ORef, String> dataToUse)
	{
		data = new HashMap<ORef, String>(dataToUse);
	}

	private void copyFromJson(EnhancedJsonObject json)
	{
		data.clear();
		EnhancedJsonObject array = json.optJson(TAG_STRING_REF_MAP);
		if(array == null)
			array = new EnhancedJsonObject();
		
		Iterator iterator = array.keys();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			add(ORef.createFromString(key), (String)array.get(key));
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonObject array = new EnhancedJsonObject();
		
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			ORef key = (ORef)iterator.next();
			array.put(key.toString(), get(key));
		}
		
		json.put(TAG_STRING_REF_MAP, array);
		return json;
	}

	public int size()
	{
		return data.size();
	}

	public void add(ORef refAsKey, String object)
	{
		data.put(refAsKey, object);
	}

	public HashMap toHashMap()
	{
		return data;
	}
	
	public String get(ORef refAsKey)
	{
		String value = (String) data.get(refAsKey);
		if (value == null)
			return "";
		
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

	public ORef findKey(String object)
	{
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			ORef key = (ORef) iterator.next();
			if (object.equals(data.get(key)))
				return key;
		}
		
		return ORef.INVALID;
	}

	
	public boolean containsValue(String value)
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

	private HashMap data;
}
