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
package org.miradi.utils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class CodeList
{
	public CodeList()
	{
		this(new Vector());
	}
	
	public CodeList(String[] codes)
	{
		this(new Vector(Arrays.asList(codes)));
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

	
	public void add(String code)
	{
		data.add(code);
	}
	
	public void addAll(CodeList listToAdd)
	{
		data.addAll(Arrays.asList(listToAdd.toArray()));
	}
		
	public String get(int index)
	{
		return data.get(index);
	}
	
	public boolean contains(String code)
	{
		return data.contains(code);
	}
	
	public int find(String code)
	{
		return data.indexOf(code);
	}
	
	public void removeCode(String code)
	{
		if(!data.contains(code))
			throw new RuntimeException("Attempted to remove non-existant code: " + code + " from: " + toString());
		data.remove(code);
	}
	
	public void subtract(CodeList other)
	{
		for(int i = 0; i < other.size(); ++i)
		{
			String code = other.get(i);
			if(contains(code))
				removeCode(code);
		}
	}
	
	public void retainAll(CodeList other)
	{
		data.retainAll(other.toVector());
	}
	
	public Vector toVector()
	{
		return new Vector(data);
	}
	
	public String[] toArray()
	{
		return data.toArray(new String[0]);
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

	Vector<String> data;

}

