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

import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;

public class DateRangeEffortList
{
	public DateRangeEffortList()
	{
		this(new Vector());
	}
	
	public DateRangeEffortList(String listAsJsonString) throws Exception
	{
		this(new EnhancedJsonObject(listAsJsonString));
	}
	
	public DateRangeEffortList(EnhancedJsonObject json) throws Exception
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_DATERANGE_EFFORTS);
		for(int i = 0; i < array.length(); ++i)
			add(new DateRangeEffort(array.getJson(i)));
	}
	
	private DateRangeEffortList(List listToUse)
	{
		data = new Vector(listToUse);
	}
	
	public DateRange getCombinedDateRange() throws Exception
	{
		if (data.size() == 0)
			return null;
		
		DateRange combinedDateRange = null; 
		for (int i = 0; i < data.size(); ++i)
		{
			DateRangeEffort dateRangeEffort = data.get(i);
			DateRange dateRange = dateRangeEffort.getDateRange();
			combinedDateRange = DateRange.combine(combinedDateRange, dateRange);
		}
		
		return combinedDateRange;
	}
	
	public DateRangeEffort getDateRangeEffortForSpecificDateRange(DateRange dateRangeToUse)
	{
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort dateRangeEffort = data.get(i);
			DateRange dateRange = dateRangeEffort.getDateRange();
			if (dateRange.equals(dateRangeToUse))
				return dateRangeEffort;
		}

		return null;
	}
	
	public double getTotalUnitQuantity(DateRange boundryDateRange)
	{
		OptionalDouble totalUnitQuantity = getOptionalTotalUnitQuantity(boundryDateRange);
		if (totalUnitQuantity.hasValue())
			return totalUnitQuantity.getValue();
		
		return 0.0;
	}
	
	public OptionalDouble getOptionalTotalUnitQuantity(DateRange dateRangeToUse)
	{
		OptionalDouble totalUnits = new OptionalDouble();
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort effort = data.get(i);
			DateRange dateRange = effort.getDateRange();
			if (dateRangeToUse.contains(dateRange))
				totalUnits = totalUnits.addValue(effort.getUnitQuantity());
		}
		return totalUnits;
	}
	
	public double getTotalUnitQuantity()
	{
		OptionalDouble totalUnitQuantity = getOptionalTotalUnitQuantity();
		if (totalUnitQuantity.hasValue())
			return totalUnitQuantity.getValue();
		
		return 0.0;
	}
	
	public OptionalDouble getOptionalTotalUnitQuantity()
	{
		OptionalDouble totalUnits = new OptionalDouble();
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort effort = data.get(i);
			totalUnits = totalUnits.addValue(effort.getUnitQuantity());
		}
		
		return totalUnits;
	}
		
	public void setDateRangeEffort(DateRangeEffort dateRangeEffortToUse)
	{
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort dateRangeEffort = data.get(i);
			if (dateRangeEffort.getDateRange().equals(dateRangeEffortToUse.getDateRange()))
			{
				data.set(i, dateRangeEffortToUse);
				return;
			}
		}
		add(dateRangeEffortToUse);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < data.size(); i++)
		{
			array.put(get(i).toJson());
		}
		json.put(TAG_DATERANGE_EFFORTS, array);
		return json;
	}
	
	public void add(DateRangeEffort dateRangeEffortToUse)
	{
		data.add(dateRangeEffortToUse);
	}
	
	public void remove(DateRange dateRange)
	{
		for(DateRangeEffort dateRangeEffort : data)
		{
			if(dateRangeEffort.getDateRange().equals(dateRange))
			{
				data.remove(dateRangeEffort);
				return;
			}
		}
	}

	public DateRangeEffort get(int index)
	{
		return data.get(index);
	}
		
	public String toString()
	{
		if(size() == 0)
			return "";
		
		return toJson().toString();
	}
	
	public boolean equals(Object other)
	{	
		if (! (other instanceof DateRangeEffortList))
			return false;
		
		return other.toString().equals(toString());	
	}
	
	public boolean contains(DateRangeEffort dateRangeEffort)
	{
		return data.contains(dateRangeEffort);
	}
	
	public int hashCode()
	{
		return data.hashCode();
	}
		
	public int size()
	{
		return data.size();
	}
	
	private Vector<DateRangeEffort> data;
	private static final String TAG_DATERANGE_EFFORTS = "DateRangeEfforts";
}
