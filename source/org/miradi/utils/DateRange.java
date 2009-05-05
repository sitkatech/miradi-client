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

import org.martus.util.MultiCalendar;

public class DateRange
{
	public DateRange(MultiCalendar startDateToUse, MultiCalendar endDateToUse) throws Exception
	{
		startDate = startDateToUse;
		endDate = endDateToUse;
		validateDate();
	}
	
	public DateRange(EnhancedJsonObject json) throws Exception
	{
		this(createDateFromJson(json, TAG_START_DATE), createDateFromJson(json, TAG_END_DATE));
	}
	
	public DateRange(DateRange other) throws Exception
	{
		this(new MultiCalendar(other.getStartDate()), new MultiCalendar(other.getEndDate()));
	}
	
	public static DateRange createFromJson(EnhancedJsonObject json) throws Exception 
	{
		if (json == null)
			return null;
		
		if (!json.has(TAG_START_DATE) || !json.has(TAG_END_DATE))
			return null;
		
		String startDateAsString = json.get(TAG_START_DATE).toString();
		String endDateAsString = json.get(TAG_END_DATE).toString();
		if (startDateAsString.length() == 0 || endDateAsString.length() == 0)
			return null;
		
		MultiCalendar startIsoDate = MultiCalendar.createFromIsoDateString(startDateAsString);
		MultiCalendar endIsoDate = MultiCalendar.createFromIsoDateString(endDateAsString);
		
		return new DateRange(startIsoDate, endIsoDate);
	}
	
	private static MultiCalendar createDateFromJson(EnhancedJsonObject json, String tag)
	{
		return MultiCalendar.createFromIsoDateString(json.get(tag).toString());
	}
	
	private void validateDate() throws Exception
	{
		if (startDate.after(endDate))
			throw new Exception("Start " + startDate + " date is after end date " + endDate);
	}

	public MultiCalendar getStartDate()
	{
		return startDate;
	}
	
	public MultiCalendar getEndDate()
	{
		return endDate;
	}
	
	public String getLabel()
	{
		return startDate.toString() +" - "+ endDate.toString();
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_START_DATE, startDate.toIsoDateString());
		json.put(TAG_END_DATE, endDate.toIsoDateString());
		return json;
	}
	
	public String toString()
	{
		int year = startDate.getGregorianYear();
		
		if(startDate.equals(endDate))
			return startDate.toIsoDateString();

		MultiCalendar nextDate = new MultiCalendar(endDate);
		nextDate.addDays(1);
		
		int startMonth = startDate.getGregorianMonth();
		int startDay = startDate.getGregorianDay();
		int nextMonth = nextDate.getGregorianMonth();
		int nextDay = nextDate.getGregorianDay();
		
		if(startDay != 1 || nextDay != 1)
			return fullDateRangeString();

		String yearString = Integer.toString(year);
		if(startMonth == 1 && nextMonth == 1)
			return yearString;
		
		if(startMonth == nextMonth && startDate.getGregorianYear()+1 == endDate.getGregorianYear())
			return "FY " + endDate.toIsoDateString().substring(0, 4);
		
		if(year != endDate.getGregorianYear())
			return fullDateRangeString();
		
		if(startMonth == endDate.getGregorianMonth())
			return startDate.toIsoDateString().substring(0, 7);
		
		if(endDate.getGregorianMonth() != startMonth + 2)
			return fullDateRangeString();
		
		if(startMonth == 1)
			return "Q1 " + yearString;
		if(startMonth == 4)
			return "Q2 " + yearString;
		if(startMonth == 7)
			return "Q3 " + yearString;
		if(startMonth == 10)
			return "Q4 " + yearString;

		return fullDateRangeString();
	}
	
	public String fullDateRangeString()
	{
		return startDate.toIsoDateString() + " - " + endDate.toIsoDateString();
	}
	
	private boolean containsEnd(MultiCalendar end)
	{
		if (end.before(endDate) || end.equals(endDate))
			return true;
		
		return false;
	}
	
	private boolean containsStart(MultiCalendar start)
	{
		if (startDate.before(start) || startDate.equals(start))
			return true;
		
		return false;
	}
	
	public boolean contains(DateRange other)
	{
		if (! containsStart(other.getStartDate()))
			return false;
		if (! containsEnd(other.getEndDate()))
			return false;
		
		return true;
	}
	
	public static DateRange combine(DateRange range1, DateRange range2) throws Exception
	{
		if (range1 == null)
			return range2;
		
		if (range2 == null)
			return range1;
			
		MultiCalendar combinedStartDate;
		if (range1.getStartDate().before(range2.getStartDate()))
			combinedStartDate = range1.getStartDate();
		else
			combinedStartDate = range2.getStartDate();
		
		MultiCalendar combinedEndDate;
		if (range1.getEndDate().after(range2.getEndDate()))
			combinedEndDate = range1.getEndDate();
		else 
			combinedEndDate = range2.getEndDate();
		
		return new DateRange(combinedStartDate, combinedEndDate);
	}
	
	public static int getYearsInBetween(MultiCalendar date1, MultiCalendar date2)
	{
		return date2.getGregorianYear() - date1.getGregorianYear();
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof DateRange))
			return false;
		
		return toString().equals(rawOther.toString());
	}
	
	public static DateRange createFromJson(MultiCalendar date1, MultiCalendar date2) throws Exception
	{
		if (date1.after(date2))
			return new DateRange(date2, date1);
		
		return new DateRange(date1, date2);
	}
	
	private static final String TAG_START_DATE = "StartDate";
	private static final String TAG_END_DATE = "EndDate";
	
	MultiCalendar startDate;
	MultiCalendar endDate;
}
