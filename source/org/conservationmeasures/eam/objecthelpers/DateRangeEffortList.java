/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
	
	public double getTotalUnitsFor(DateRange dateRangeToUse)
	{
		double total = 0;
		
		for (int i = 0; i < data.size(); i++)
		{
			//TODO budget data must check for end date too.
			DateRangeEffort dateRangeEffort = (DateRangeEffort)data.get(i);
			DateRange ourDateRange = dateRangeEffort.getDateRange();
			if (ourDateRange.equals(dateRangeToUse.getStartDate()))
					total += dateRangeEffort.getUnitQuantity();
		}
		return total;
	}
	
	public double getTotalUnitQuantity()
	{
		double totalUnits = 0.0;
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort effort = (DateRangeEffort)data.get(i);
			totalUnits += effort.getUnitQuantity();
		}
		return totalUnits;
	}
		
	public void setDateRangeEffort(DateRangeEffort dateRangeEffortToUse)
	{
		for (int i = 0; i < data.size(); i++)
		{
			DateRangeEffort dateRangeEffort = (DateRangeEffort)data.get(i);
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
		return (DateRangeEffort)data.get(index);
	}
		
	public String toString()
	{
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
	
	private Vector data;
	private static final String TAG_DATERANGE_EFFORTS = "DateRangeEfforts";
	
}
