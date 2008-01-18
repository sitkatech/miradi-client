/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		if(newValue.length() == 0)
		{
			date = null;
			return;
		}
		
		try
		{
			date = MultiCalendar.createFromIsoDateString(newValue);
		}
		catch (Exception e)
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
	
	public MultiCalendar getDate()
	{
		return date;
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof DateData))
			return false;
		
		DateData other = (DateData)rawOther;
		return date.equals(other.date);
	}

	public int hashCode()
	{
		return date.hashCode();
	}
	
	private MultiCalendar date;
}
