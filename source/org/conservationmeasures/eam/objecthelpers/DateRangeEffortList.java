/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;

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
	
	public DateRangeEffort getEffortForDateRange(DateRange dateRangeToUse)
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
		double totalUnits = 0.0;
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort effort = data.get(i);
			DateRange dateRange = effort.getDateRange();
			if (boundryDateRange.contains(dateRange))
				totalUnits += effort.getUnitQuantity();
		}
		return totalUnits;
	}
	
	public double getTotalUnitQuantity()
	{
		double totalUnits = 0.0;
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort effort = data.get(i);
			totalUnits += effort.getUnitQuantity();
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
				dateRangeEffort.setUnitQuantity(dateRangeEffortToUse.getUnitQuantity());
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
	
	public DateRangeEffortList cloneShiftedByMonths(int monthDelta) throws Exception
	{
		DateRangeEffortList clone = new DateRangeEffortList();
		for(int i = 0; i < size(); ++i)
		{
			DateRangeEffort effort = get(i);
			clone.add(effort.cloneShiftedByMonths(monthDelta));
		}
		return clone;
	}	

	private Vector<DateRangeEffort> data;
	private static final String TAG_DATERANGE_EFFORTS = "DateRangeEfforts";
}
