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
package org.miradi.objectdata;

import org.miradi.objecthelpers.DateRangeEffortList;

public class DateRangeEffortListData extends ObjectData
{
	public DateRangeEffortListData(String tagToUse)
	{
		super(tagToUse);
		dateRangeEffortList = new DateRangeEffortList();
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
