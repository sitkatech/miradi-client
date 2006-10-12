/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.martus.util.MultiCalendar;

public class DateData extends ObjectData
{
	public DateData()
	{
		date = null;
	}

	public DateData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch (Exception e)
		{
			EAM.logDebug("DateData ignoring invalid: " + valueToUse);
		}
	}

	public void set(String newValue) throws Exception
	{
		try
		{
			date = MultiCalendar.createFromIsoDateString(newValue);
		}
		catch (NumberFormatException e)
		{
			throw new InvalidDateException(e);
		}
	}

	public String get()
	{
		if(date == null)
			return "";
		return date.toIsoDateString();
	}

	MultiCalendar date;
}
