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

import org.martus.util.MultiCalendar;
import org.miradi.main.EAMTestCase;
import org.miradi.utils.DateRange;

public class TestDateRangeData extends EAMTestCase
{
	public TestDateRangeData(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2006, 6, 6);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2007, 7, 7);
		DateRange dateRange = new DateRange(start, end);
		DateRangeData dateRangeData = new DateRangeData("");
		dateRangeData.set(dateRange.toJson().toString());
		assertEquals("not same date range?", dateRange, dateRangeData.getDateRange());
		
		DateRangeData dateRangeData2 = new DateRangeData("");
		dateRangeData2.set(dateRangeData.toString());
		assertEquals("different jsons?", dateRangeData.toString(), dateRangeData2.toString());
	}
}
