/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;


public class DateRangeEffort
{
	
	public DateRangeEffort(int costUnitToUse, float unitQuantityToUse, DateRange dateRangeToUse)
	{
		costUnitCode = costUnitToUse;
		numberOfUnits = unitQuantityToUse;
		dateRange = dateRangeToUse;
	}
	
	public DateRangeEffort(EnhancedJsonObject json) throws Exception 
	{
		costUnitCode = json.getInt(TAG_COST_UNIT_CODE);
		numberOfUnits = json.getDouble(TAG_NUMBER_OF_UNITS);
		dateRange = new DateRange(json.getJson(TAG_DATERANGE));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		if (dateRange != null)
		{
			json.put(TAG_COST_UNIT_CODE, costUnitCode);
			json.put(TAG_DATERANGE, dateRange);
			json.put(TAG_NUMBER_OF_UNITS, numberOfUnits);
		}
		return json;	
	}
	
	public int getCostUnit()
	{
		return costUnitCode;
	}
	
	public double getUnitQuantity()
	{
		return numberOfUnits;
	}
	
	public DateRange getDateRange()
	{
		return dateRange;
	}
	
	public String toString()
	{
		return toJson().toString();
	}
	
	private static final String TAG_COST_UNIT_CODE = "CostUnitCode";
	private static final String TAG_DATERANGE = "DateRange";
	private static final String TAG_NUMBER_OF_UNITS = "NumberOfUnits";
	
	private int costUnitCode;
	private DateRange dateRange;
	private double numberOfUnits;	
}
