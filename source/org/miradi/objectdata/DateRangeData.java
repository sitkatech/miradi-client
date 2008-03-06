/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class DateRangeData extends ObjectData
{
	public DateRangeData(String tagToUse)
	{
		super(tagToUse);
		dateRange = null;
	}

	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			dateRange = null;
			return;
		}
		
		dateRange = new DateRange(new EnhancedJsonObject(newValue));
	}

	public String get()
	{
		if(dateRange == null)
			return "";
		
		return dateRange.toJson().toString();
	}
	
	public DateRange getDateRange()
	{
		return dateRange;
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof DateData))
			return false;
		
		DateRangeData other = (DateRangeData)rawOther;
		return dateRange.equals(other.dateRange);
	}

	public int hashCode()
	{
		return dateRange.hashCode();
	}
	
	private DateRange dateRange;
}
