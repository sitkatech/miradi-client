/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

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
	
	private static MultiCalendar createDateFromJson(EnhancedJsonObject json, String tag)
	{
		return MultiCalendar.createFromIsoDateString(json.get(tag).toString());
	}
	
	private void validateDate() throws Exception
	{
		if (startDate.before(endDate))
			return;
		
		throw new Exception();
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
		// FIXME: Really crude (but effective) hack!
		if(startDate.getGregorianYear() == endDate.getGregorianYear())
		{
			if(startDate.getGregorianMonth() == 1 && startDate.getGregorianDay() == 1)
			{
				if(endDate.getGregorianMonth() == 12 && endDate.getGregorianDay() == 31)
					return Integer.toString(startDate.getGregorianYear());
				else if(endDate.getGregorianMonth() == 3 && endDate.getGregorianDay() == 31)
					return "Q1 " + Integer.toString(startDate.getGregorianYear());
			}
			else if(startDate.getGregorianMonth() == 4 && startDate.getGregorianDay() == 1)
			{
				if(endDate.getGregorianMonth() == 6 && endDate.getGregorianDay() == 30)
					return "Q2 " + Integer.toString(startDate.getGregorianYear());
			}
			else if(startDate.getGregorianMonth() == 7 && startDate.getGregorianDay() == 1)
			{
				if(endDate.getGregorianMonth() == 9 && endDate.getGregorianDay() == 30)
					return "Q3 " + Integer.toString(startDate.getGregorianYear());
			}
			else if(startDate.getGregorianMonth() == 10 && startDate.getGregorianDay() == 1)
			{
				if(endDate.getGregorianMonth() == 12 && endDate.getGregorianDay() == 31)
					return "Q4 " + Integer.toString(startDate.getGregorianYear());
			}

		}
		
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
	
	private static final String TAG_START_DATE = "StartDate";
	private static final String TAG_END_DATE = "EndDate";
	
	MultiCalendar startDate;
	MultiCalendar endDate;
}
