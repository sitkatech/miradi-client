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

import java.util.Calendar;
import java.util.GregorianCalendar;
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
	
	private String getDateUnitCode()
	{
		return dateUnit;
	}
	
	public boolean isBlank()
	{
		return getDateUnitCode().length() == 0;
	}
	
	private boolean isYear()
	{
		return getDateUnitCode().matches("\\d\\d\\d\\d");
	}
	
	private boolean isQuarter()
	{
		String code = getDateUnitCode();
		return code.matches("\\d\\d\\d\\dQ\\d");
	}
	
	private boolean isMonth()
	{
		String code = getDateUnitCode();
		return code.matches("\\d\\d\\d\\d-\\d\\d");
	}
	
	public boolean isDay()
	{
		String code = getDateUnitCode();
		return code.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d");
	}

	public DateRange asDateRange() throws Exception
	{
		if (isYear())
			return getYearDateRange(); 
		
		if (isQuarter())
			return getQuarterDateRange();
		
		if (isMonth())
			return getMonthDateRange();
		
		if (isDay())
			return getDayDateRange();
			
		throw new Exception(EAM.text("No date range for date Unit = " + dateUnit.toString()));
	}
	
	private DateRange getMonthDateRange() throws Exception
	{	
 		int year = getYear();
		int month = getMonth();
	    int daysInMonth = getNumberOfDaysInMonth(year, month); 
	    
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(year, month, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(year, month, daysInMonth);
		
		return new DateRange(startDate, endDate);			
	}
	
	private DateRange getDayDateRange() throws Exception
	{
		MultiCalendar date = MultiCalendar.createFromGregorianYearMonthDay(getYear(), getMonth(), getDay());
		return new DateRange(date, date);
	}

	private int getNumberOfDaysInMonth(int year, int month)
	{
		int zeroOffsetMonth = month - 1;
		Calendar calendar = new GregorianCalendar(year, zeroOffsetMonth, 1);
	    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	private DateRange getYearDateRange() throws Exception
	{
		int year = Integer.parseInt(getDateUnitCode());
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(year, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(year, 12, 31);
		return new DateRange(startDate, endDate);
	}
	
	private DateRange getQuarterDateRange() throws Exception
	{
		int quarter = getQuarter();
		int startYear = getYear();
		int startMonth = (quarter - 1) * 3;
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(startYear, startMonth+1, 1);
		
		int pastEndMonth = (startMonth + 3) % 12;
		int endYear = (pastEndMonth == 0 ? startYear+1 : startYear);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(endYear, pastEndMonth+1, 1);
		endDate.addDays(-1);
		
		return new DateRange(startDate, endDate);
	}

	private int getYear()
	{
		return Integer.parseInt(getDateUnitCode().substring(0, 4));
	}
	
	public int getMonth()
	{
		String dateUnitCodeToParse = getDateUnitCode();
		int indexOfYearMonthSeperator = dateUnitCodeToParse.indexOf("-");
		int length = dateUnitCodeToParse.length();
		String monthDayString = dateUnitCodeToParse.substring(indexOfYearMonthSeperator + 1, length);
		int indexOfMonthDaySeperator = monthDayString.indexOf("-");
		if (indexOfMonthDaySeperator < 0)
			return Integer.parseInt(monthDayString);
		
		return Integer.parseInt(monthDayString.substring(0, indexOfMonthDaySeperator));
	}
	
	public int getDay()
	{
		int lastIndexOf = getDateUnitCode().lastIndexOf("-");
		int length = getDateUnitCode().length();
		
		return parseDateUnit(lastIndexOf + 1, length);
	}

	private int parseDateUnit(int beginIndex, int endIndex)
	{
		return Integer.parseInt(getDateUnitCode().substring(beginIndex, endIndex));
	}

	private int getQuarter()
	{
		int quarter = Integer.parseInt(getDateUnitCode().substring(5));
		return quarter;
	}
	
	public boolean hasSubDateUnits()
	{
		if(isYear())
			return true;
		
		if(isQuarter())
			return true;
		
		if (isMonth())
			return true;
		
		return false;
	}

	public Vector<DateUnit> getSubDateUnits() throws Exception
	{
		if(isYear())
			return getYearSubDateUnits();
		
		if(isQuarter())
			return getQuarterSubDateUnits();
		
		if(isMonth())
			return getMonthSubDateUnits();
		
		throw new Exception("Can't call getSubDateUnits for DateUnit: " + getDateUnitCode());
	}

	private Vector<DateUnit> getYearSubDateUnits()
	{
		Vector<DateUnit> quarters = new Vector<DateUnit>();
		for(int index = 0; index < 4; ++index)
		{
			quarters.add(new DateUnit(getDateUnitCode() + "Q" + (index+1)));
		}
		
		return quarters;
	}
	
	private Vector<DateUnit> getQuarterSubDateUnits()
	{
		Vector<DateUnit> months = new Vector<DateUnit>();
		int quarter = getQuarter();
		String[] monthStrings = monthsPerQuarter[quarter-1];
		for(int index = 0; index < monthStrings.length; ++index)
		{
			months.add(new DateUnit(getYear() + "-" + monthStrings[index]));
		}
		
		return months;
	}
	
	private Vector<DateUnit> getMonthSubDateUnits()
	{
		Vector<DateUnit> days = new Vector<DateUnit>();
		int year = getYear();
		String month = monthsPerYear[getMonth()-1];
		int daysInMonth = getNumberOfDaysInMonth(year, getMonth());
		int daysInMonthArrayCount = daysInMonth - 1;
		for(int day = 0; day <= daysInMonthArrayCount; ++day)
		{
			days.add(new DateUnit(getYear() + "-" + month + "-" + daysPerMonth[day]));
		}
		
		return days;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DateUnit))
			return false;
		
		DateUnit thisDateUnit = (DateUnit) obj;
		return thisDateUnit.getDateUnitCode().equals(getDateUnitCode());
	}
	
	@Override
	public String toString()
	{
		return getDateUnitCode().toString();
	}
	
	private static final String[][] monthsPerQuarter = { 
		{"01", "02", "03"}, 
		{"04", "05", "06"}, 
		{"07", "08", "09"}, 
		{"10", "11", "12"}, 
	};
	
	private static final String[] monthsPerYear = {"01", "02", "03","04", "05", "06", "07", "08", "09", "10", "11", "12"};
	private static final String[] daysPerMonth = {"01", "02", "03","04", "05", "06", "07", "08", "09", "10", "11", "12", 
												  "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", 
												  "25", "26", "27", "28", "29", "30", "31",};
	private String dateUnit;
}
