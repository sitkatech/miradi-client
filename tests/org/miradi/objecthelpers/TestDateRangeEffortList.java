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

import org.martus.util.MultiCalendar;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class TestDateRangeEffortList extends EAMTestCase
{
	public TestDateRangeEffortList(String name)
	{
		super(name);
	}
	
	public void testGetCombinedDateRange() throws Exception
	{
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		DateRange combinedDateRange = dateRangeEffortList.getCombinedDateRange();
		assertNull("combinedDateRange was not null?", combinedDateRange);
		
		DateRangeEffortList dateRangeEffortList2 = new DateRangeEffortList();
		dateRangeEffortList2.add(createDateRangeEffort());
		DateRange combinedDateRange2 = dateRangeEffortList2.getCombinedDateRange();
		assertNotNull("combinedDateRange2 was null?", combinedDateRange2);
		
		MultiCalendar expectedStartDate = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		assertEquals("wrong start date?", expectedStartDate, combinedDateRange2.getStartDate());
		
		MultiCalendar expectedEndDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		assertEquals("wrong end date?", expectedEndDate, combinedDateRange2.getEndDate());
	}
	
	private DateRangeEffort createDateRangeEffort() throws Exception
	{
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		DateRange dateRange = new DateRange(startDate, endDate);
		
		return new DateRangeEffort("", 1, dateRange);
	}
}
