/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

public class DateRangeEffort
{
	public DateRangeEffort(String costUnitToUse, double unitQuantityToUse, DateRange dateRangeToUse)
	{
		costUnitCode = costUnitToUse;
		numberOfUnits = unitQuantityToUse;
		dateRange = dateRangeToUse;
	}
	
	public DateRangeEffort(EnhancedJsonObject json) throws Exception 
	{
		costUnitCode = json.getString(TAG_COST_UNIT_CODE);
		numberOfUnits = json.getDouble(TAG_NUMBER_OF_UNITS);
		dateRange = new DateRange(json.getJson(TAG_DATERANGE));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		if (dateRange != null)
		{
			json.put(TAG_COST_UNIT_CODE, costUnitCode);
			json.put(TAG_DATERANGE, dateRange.toJson());
			json.put(TAG_NUMBER_OF_UNITS, numberOfUnits);
		}
		return json;	
	}
	
	public String getCostUnit()
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
	
	public void setCostUnit(String costUnitCodeToUse)
	{
		costUnitCode = costUnitCodeToUse;
	}
	
	public void setUnitQuantity(double unitQuantity)
	{
		numberOfUnits = unitQuantity;
	}
	
	public String toString()
	{
		return toJson().toString();
	}
	
	private static final String TAG_COST_UNIT_CODE = "CostUnitCode";
	private static final String TAG_DATERANGE = "DateRange";
	private static final String TAG_NUMBER_OF_UNITS = "NumberOfUnits";
	
	private String costUnitCode;
	private DateRange dateRange;
	private double numberOfUnits;
}
