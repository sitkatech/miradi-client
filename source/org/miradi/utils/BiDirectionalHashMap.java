/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.util.HashMap;
import java.util.HashSet;

public class BiDirectionalHashMap
{
	public BiDirectionalHashMap()
	{
		keyToValueMap = new HashMap<String, String>();
		valueToKeyMap = new HashMap<String, String>();
	}
	
	public void put(String key, String value)
	{		
		keyToValueMap.remove(valueToKeyMap.get(value));
		valueToKeyMap.remove(keyToValueMap.get(key));
		
		keyToValueMap.put(key, value);
		valueToKeyMap.put(value, key);
	}
	
	public String getKey(String value)
	{
		return valueToKeyMap.get(value);
	}
	
	public String getValue(String key)
	{
		return keyToValueMap.get(key);
	}
	
	public boolean containsKey(String key)
	{
		return keyToValueMap.containsKey(key);
	}
	
	public boolean containsValue(String valueAsKey)
	{
		return valueToKeyMap.containsKey(valueAsKey);
	}
	
	public int size() throws Exception
	{
		if (keyToValueMap.size() !=  valueToKeyMap.size())
			throw new Exception("BiDirectional maps should be the same size");
		
		return keyToValueMap.size();
	}
	
	public HashSet<String> getKeys()
	{
		return new HashSet<String>(keyToValueMap.keySet());
	}
	
	public BiDirectionalHashMap reverseMap()
	{
		BiDirectionalHashMap reverseMap = new BiDirectionalHashMap();
		HashSet<String> keys = getKeys();
		for(String key : keys)
		{
			reverseMap.put(getValue(key), key);
		}

		return reverseMap;
	}
	
	private HashMap<String, String> keyToValueMap;
	private HashMap<String, String> valueToKeyMap;
}
