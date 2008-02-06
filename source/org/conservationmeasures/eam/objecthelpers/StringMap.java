/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class StringMap
{
	public StringMap()
	{
		this(new HashMap());
	}

	public StringMap(StringMap copyFrom)
	{
		this(new HashMap(copyFrom.data));
	}

	public StringMap(EnhancedJsonObject json)
	{
		this();
		copyFromJson(json);
	}
	
	public StringMap(String mapAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(mapAsJsonString));
	}
	
	private StringMap(Map dataToUse)
	{
		data = new HashMap(dataToUse);
	}

	private void copyFromJson(EnhancedJsonObject json)
	{
		data.clear();
		EnhancedJsonObject array = json.optJson(TAG_STRING_MAP);
		if(array == null)
			array = new EnhancedJsonObject();
		Iterator iterator = array.keys();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			add(key, (String)array.get(key));
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonObject array = new EnhancedJsonObject();
		
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			array.put(key, get(key));
		}
		json.put(TAG_STRING_MAP, array);
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

	public HashMap toHashMap()
	{
		return data;
	}
	
	public String get(String code)
	{
		String value = (String)data.get(code);
		if (value==null)
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

	public String find(String object)
	{
		Iterator iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			if (object.equals(data.get(key)))
				return key;
		}
		return null;
	}

	
	public boolean contains(String code)
	{
		return data.containsKey(code);
	}

	public void removeCode(String code)
	{
		if(!data.containsKey(code))
			throw new RuntimeException(
					"Attempted to remove non-existant code: " + code
							+ " from: " + toString());
		data.remove(code);
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringMap))
			return false;

		StringMap other = (StringMap) rawOther;
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

	private static final String TAG_STRING_MAP = "StringMap";

	private HashMap data;
}
