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

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.utils.EnhancedJsonObject;

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
		String value = data.get(code);
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
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
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
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.writeln("<StringMap>");
		Set keys = data.keySet();
		for(Object object : keys)
		{
			out.write("<Item code='" + XmlUtilities.getXmlEncoded(object.toString()) + "'>");
			out.write("<Value>");
			
			String rawValue = data.get(object.toString()).toString();
			out.write(XmlUtilities.getXmlEncoded(rawValue));
			
			out.write("</Value>");
			out.writeln("</Item>");
		}
		out.writeln("</StringMap>");
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

	private HashMap<String, String> data;
}
