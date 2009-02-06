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
package org.miradi.utils;

import org.martus.util.MultiCalendar;
import org.miradi.main.EAMTestCase;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class TestDateRange extends EAMTestCase
{
	public TestDateRange(String name) throws Exception
	{
		super(name);
	}
	
	public void test() throws Exception
	{
		storeAndRestore();	
	}
	
	public void testCreateFromJson() throws Exception
	{
		DateRange dateRange = getSampleDateRange();
		DateRange dateRangeFromJson = DateRange.createFromJson(dateRange.toJson());
		assertEquals("not same date range?", dateRange, dateRangeFromJson);
			
		assertNull("not null?", DateRange.createFromJson(null));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject()));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject("{notRelevant:\"\"}")));
	}
	
	private void storeAndRestore() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2006, 12, 1);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2006, 12, 2);
		DateRange dateRange = new DateRange(start, end);
		
		EnhancedJsonObject json = dateRange.toJson();
		
		DateRange dateRange2 = new DateRange(json);
		assertEquals("start date is same?", start, dateRange2.getStartDate());
		assertEquals("end date is same?", end, dateRange2.getEndDate());	
	}
	
	public void testIsWithinBounds() throws Exception
	{
		DateRange boundsDateRange = getSampleDateRange();
		
		MultiCalendar innerStartDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 5);
		MultiCalendar innerEndDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 10);
		DateRange innerDateRange = new DateRange(innerStartDate, innerEndDate);
		
		MultiCalendar partialInnerStartDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 15);
		MultiCalendar partialInnerEndDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 25);
		DateRange  partialyInDateRange = new DateRange(partialInnerStartDate, partialInnerEndDate);
		
		assertEquals("is within bounds?", true, boundsDateRange.contains(innerDateRange));
		assertEquals("is within bounds?", false, boundsDateRange.contains(partialyInDateRange));
		assertEquals("contains itself?", true, boundsDateRange.contains(boundsDateRange));
	}

	private DateRange getSampleDateRange() throws Exception
	{
		MultiCalendar boundsStartDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 1);
		MultiCalendar boundsEndDate = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 20);
		
		DateRange boundsDateRange = new DateRange(boundsStartDate, boundsEndDate);
		return boundsDateRange;
	}
	
	public void testCombine() throws Exception
	{
		MultiCalendar dateRange1Start = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 5);
		MultiCalendar dateRange1End = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 10);
		DateRange dateRange1 = new DateRange(dateRange1Start, dateRange1End);
		
		MultiCalendar dateRange2Start = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 15);
		MultiCalendar dateRange2End = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 20);
		DateRange dateRange2 = new DateRange(dateRange2Start, dateRange2End);	
		
		DateRange combined = new DateRange(dateRange1Start, dateRange2End); 
		
		assertEquals("combined date ranges?", combined, DateRange.combine(dateRange1, dateRange2));
		assertEquals("combine with self is same?", dateRange1, DateRange.combine(dateRange1, dateRange1));
	}
	
	public void testGetYearsInBetween() throws Exception
	{
		MultiCalendar date1 = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 1);
		MultiCalendar date2 = MultiCalendar.createFromGregorianYearMonthDay(2008, 1, 1);
		
		assertEquals("count years in betweem?", 2, DateRange.getYearsInBetween(date1, date2));
	}
	
}