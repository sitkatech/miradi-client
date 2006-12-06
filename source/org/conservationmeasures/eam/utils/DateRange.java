/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
	
	private static final String TAG_START_DATE = "StartDate";
	private static final String TAG_END_DATE = "EndDate";
	
	MultiCalendar startDate;
	MultiCalendar endDate;
}
