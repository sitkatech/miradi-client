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

import org.miradi.objecthelpers.DateUnit;

public class DateUnitEffort
{
	public DateUnitEffort(double unitQuantityToUse, DateUnit dateUnitToUse)
	{
		numberOfUnits = unitQuantityToUse;
		dateUnit = dateUnitToUse;
	}
	
	public DateUnitEffort(EnhancedJsonObject json) throws Exception 
	{
		numberOfUnits = json.getDouble(TAG_NUMBER_OF_UNITS);
		dateUnit = new DateUnit(json.getJson(TAG_DATEUNIT));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_DATEUNIT, dateUnit.toJson());
		json.put(TAG_NUMBER_OF_UNITS, numberOfUnits);
		
		return json;	
	}
	
	public double getUnitQuantity()
	{
		return numberOfUnits;
	}
	
	public DateUnit getDateUnit()
	{
		return dateUnit;
	}
	
	public void setUnitQuantity(double unitQuantity)
	{
		numberOfUnits = unitQuantity;
	}
	
	@Override
	public String toString()
	{
		return toJson().toString();
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (!(rawOther instanceof DateUnitEffort))
			return false;
		
		DateUnitEffort other = (DateUnitEffort) rawOther;
		return toString().equals(other.toString());
	}
	
	private static final String TAG_DATEUNIT = "DateUnit";
	private static final String TAG_NUMBER_OF_UNITS = "NumberOfUnits";
	
	private DateUnit dateUnit;
	private double numberOfUnits;
}
