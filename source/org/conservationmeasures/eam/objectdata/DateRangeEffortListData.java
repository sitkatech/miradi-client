/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;

public class DateRangeEffortListData extends ObjectData
{
	public DateRangeEffortListData()
	{
		dateRangeEffortList = new DateRangeEffortList();
	}
	
	public DateRangeEffortListData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch(Exception e)
		{
			EAM.logWarning("DateRangeEffortListData ignoring invalid: " + valueToUse);
		}
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof DateRangeEffortListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}
	
	public DateRangeEffortList getDateRangeEffortList()
	{
		return dateRangeEffortList; 
	}
	 
	

	public String get()
	{
		return dateRangeEffortList.toString();
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	public void set(String newValue) throws Exception
	{
		set(new DateRangeEffortList(newValue));	
	}
	
	private void set(DateRangeEffortList dateRangeEffortToUse)
	{
		dateRangeEffortList = dateRangeEffortToUse;
	}

	private DateRangeEffortList dateRangeEffortList;
}
