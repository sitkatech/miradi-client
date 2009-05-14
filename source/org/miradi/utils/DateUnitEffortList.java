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

import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.miradi.objecthelpers.DateRangeEffortList;

public class DateUnitEffortList
{
	public DateUnitEffortList()
	{
		this(new Vector());
	}
	
	public DateUnitEffortList(DateRangeEffortList dateRangeEffortList)
	{
		for (int index = 0; index < dateRangeEffortList.size(); ++index)
		{
			DateRangeEffort dateRangeEffort = dateRangeEffortList.getDateRangeEffort(index);
			DateUnitEffort dateUnitEffort = new DateUnitEffort(dateRangeEffort);
			add(dateUnitEffort);
		}
	}
	
	public DateUnitEffortList(String listAsJsonString) throws Exception
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	public DateUnitEffortList(EnhancedJsonObject json) throws Exception
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_DATEUNIT_EFFORTS);
		for(int index = 0; index < array.length(); ++index)
		{
			add(new DateUnitEffort(array.getJson(index)));
		}
	}
	
	private DateUnitEffortList(List listToUse)
	{
		data = new Vector(listToUse);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for(DateUnitEffort dateUnitEffort : data)
		{
			array.put(dateUnitEffort.toJson());
		}
		json.put(TAG_DATEUNIT_EFFORTS, array);
		
		return json;
	}
	
	public void add(DateUnitEffort dateUnitEffortToUse)
	{
		data.add(dateUnitEffortToUse);
	}

	@Override
	public String toString()
	{
		if(size() == 0)
			return "";
		
		return toJson().toString();
	}
	
	@Override
	public int hashCode()
	{
		return data.hashCode();
	}
			
	@Override
	public boolean equals(Object other)
	{	
		if (! (other instanceof DateUnitEffortList))
			return false;
		
		return other.toString().equals(toString());	
	}
	
	public int size()
	{
		return data.size();
	}
	
	public DateRangeEffortList asDateRangeEffortList() throws Exception
	{
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		for(DateUnitEffort dateUnitEffort : data)
		{
			dateRangeEffortList.add(dateUnitEffort.asDateRangeEffort());
		}
		
		return dateRangeEffortList;
	}
	
	private Vector<DateUnitEffort> data;
	private static final String TAG_DATEUNIT_EFFORTS = "DateUnitEfforts";
}
