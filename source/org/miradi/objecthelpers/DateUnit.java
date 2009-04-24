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

import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.main.EAM;
import org.miradi.utils.DateRange;

public class DateUnit
{
	public DateUnit()
	{
		this("");
	}
	
	public DateUnit(String dateUnitToUse)
	{
		dateUnit = dateUnitToUse;
	}
	
	public String getDateUnit()
	{
		return dateUnit;
	}
	
	public boolean isBlank()
	{
		return getDateUnit().isEmpty();
	}
	
	private boolean isYear()
	{
		return isSameCharCount(YEAR_CHAR_COUNT);
	}

	private boolean isSameCharCount(int charCount)
	{
		return getDateUnit().length() == charCount;
	}
	
	public DateRange asDateRange() throws Exception
	{
		if (isYear())
			return getYear(); 
			
		throw new Exception(EAM.text("No date range for date Unit = " + dateUnit.toString()));
	}
	
	private DateRange getYear() throws Exception
	{
		int year = Integer.parseInt(getDateUnit());
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(year, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(year, 12, 31);
		return new DateRange(startDate, endDate);
	}
	
	public Vector<DateUnit> getSubDateUnits(DateRange dateRange) throws Exception
	{
		return new Vector();
	}
	
	public Vector<DateUnit> getProjectStartEndDateUnits(DateRange dateRange)
	{
		Vector<DateUnit> dateUnits = new Vector();
		Vector<Integer> years = extractYears(dateRange);
		for (int index = 0; index < years.size(); ++index)
		{
			dateUnits.add(new DateUnit(years.get(index).toString()));
		}
		
		return dateUnits;
	}
	
	public Vector<Integer> extractYears(DateRange dateRange)
	{
		int startYear = dateRange.getStartDate().getGregorianYear();
		int endYear = dateRange.getEndDate().getGregorianYear();
		Vector<Integer> years = new Vector<Integer>();
		for (int year = startYear; year <= endYear; ++year)
		{
			years.add(year);
		}
		
		return years;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DateUnit))
			return false;
		
		DateUnit thisDateUnit = (DateUnit) obj;
		return thisDateUnit.getDateUnit().equals(getDateUnit());
	}
	
	private String dateUnit;
	private static final int YEAR_CHAR_COUNT = 4;
}
