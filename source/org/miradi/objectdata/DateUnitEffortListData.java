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

import org.miradi.utils.DateUnitEffortList;

public class DateUnitEffortListData extends ObjectData
{
	public DateUnitEffortListData(String tagToUse)
	{
		super(tagToUse);
		dateUnitEffortList = new DateUnitEffortList();
	}
	
	public DateUnitEffortList getDateUnitEffortList()
	{
		return dateUnitEffortList; 
	}

	public String get()
	{
		return dateUnitEffortList.toString();
	}

	public void set(String newValue) throws Exception
	{
		set(new DateUnitEffortList(newValue));	
	}
	
	private void set(DateUnitEffortList dateUnitEffortToUse)
	{
		dateUnitEffortList = dateUnitEffortToUse;
	}
	
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof DateUnitEffortListData))
			return false;
		
		return rawOther.toString().equals(toString());
	}

	private DateUnitEffortList dateUnitEffortList;
}
