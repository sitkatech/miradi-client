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

public class StringList
{
	public StringList()
	{
		this(new Vector<String>());
	}
	
	public StringList(String[] codes)
	{
		this(new Vector<String>(Arrays.asList(codes)));
	}
	
	public StringList(StringList copyFrom)
	{
		this(new Vector<String>(copyFrom.data));
	}
	
	public StringList(EnhancedJsonObject json)
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(getJsonTag());
		if(array == null)
			array = new EnhancedJsonArray();
		for(int i = 0; i < array.length(); ++i)
			add((String)array.get(i));
	}
	
	public StringList(String listAsJsonString) throws ParseException
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	protected StringList(List<String> dataToUse)
	{
		data = new Vector<String>(dataToUse);
	}
	
	public int size()
	{
		return data.size();
	}
	
	public void add(String code)
	{
		data.add(code);
	}
	
	public void addIntCode(int codeAsInt)
	{
		add(Integer.toString(codeAsInt));
	}
	
	public void addAll(StringList listToAdd)
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
	
	public void subtract(StringList other)
	{
		for(int i = 0; i < other.size(); ++i)
		{
			String code = other.get(i);
			if(contains(code))
				removeCode(code);
		}
	}
	
	public void retainAll(StringList other)
	{
		data.retainAll(other.toVector());
	}
	
	public Vector<String> toVector()
	{
		return new Vector<String>(data);
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
		json.put(getJsonTag(), array);
		return json;
	}
	
	@Override
	public String toString()
	{
		if(size() == 0)
			return "";
		return toJson().toString();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof StringList))
			return false;
		
		StringList other = (StringList)rawOther;
		return data.equals(other.data);
	}
	
	@Override
	public int hashCode()
	{
		return data.hashCode();
	}
	
	public void removeCodeAt(int index)
	{
		data.removeElementAt(index);
	}
	
	public void insertElementAt(String code, int index)
	{
		data.insertElementAt(code, index);
	}
	
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	public boolean hasData()
	{
		return !isEmpty();
	}
	
	protected String getJsonTag()
	{
		return TAG_IDS;
	}
		
	private static final String TAG_IDS = "List";
	private Vector<String> data;
}
