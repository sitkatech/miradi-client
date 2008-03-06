/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
	}
}
