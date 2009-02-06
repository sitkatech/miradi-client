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
