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
		
		MultiCalendar expectedEndDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 12, 31);
		assertEquals("wrong end date?", expectedEndDate, combinedDateRange2.getEndDate());
	}
	
	public void testGetCombinedDateRangeWithZeros() throws Exception
	{
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		DateRangeEffort dre = createDateRangeEffort();
		dre.setUnitQuantity(0);
		dateRangeEffortList.add(dre);
		DateRange combinedDateRange = dateRangeEffortList.getCombinedDateRange();
		assertNotNull("combinedDateRange was null?", combinedDateRange);
	}
	
	public void testRemoveDateRangeEffort() throws Exception
	{
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		DateRange dateRange = new DateRange(startDate, endDate);
		DateRangeEffort dateRangeEffort = new DateRangeEffort("", 1, dateRange);
		
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		dateRangeEffortList.add(dateRangeEffort);
		dateRangeEffortList.remove(dateRange);
		assertEquals(0, dateRangeEffortList.size());
	}
	
	public void testSetDateRangeEffort() throws Exception
	{
		DateRangeEffortList drel = new DateRangeEffortList();
		
		DateRangeEffort dre = createDateRangeEffort();
		drel.setDateRangeEffort(dre);
		assertEquals(dre.getUnitQuantity(), drel.getTotalUnitQuantity());
		
		DateRangeEffort dre2 = createDateRangeEffort();
		dre2.setCostUnit("test");
		dre2.setUnitQuantity(22.2);
		drel.setDateRangeEffort(dre2);
		assertEquals("Didn't replace existing unit quantity?", dre2.getUnitQuantity(), drel.getTotalUnitQuantity());
		assertEquals("Didn't replace existing cost unit?", dre2.getCostUnit(), drel.getDateRangeEffortForSpecificDateRange(dre.getDateRange()).getCostUnit());
		
	}
	
	public void testMergeAdd() throws Exception
	{
		DateRange projectDateRange = createYearDateRange(2000, 2010);
		DateRangeEffortList mainList = new DateRangeEffortList();
		DateRangeEffort dre2006 = createYearDateRangeEffort(2006, 2006, 1);
		mainList.add(dre2006);
		assertEquals(dre2006.getDateRange(), mainList.getCombinedDateRange());
		
		DateRangeEffortList list2 = new DateRangeEffortList();
		DateRangeEffort dre2007 = createYearDateRangeEffort(2007, 2007, 2);
		list2.add(dre2007);
		mainList.mergeAdd(list2, projectDateRange);
		DateRange twoYears = new DateRange(dre2006.getDateRange().getStartDate(), dre2007.getDateRange().getEndDate());
		assertEquals(twoYears, mainList.getCombinedDateRange());
		
		DateRangeEffortList list3 = new DateRangeEffortList();
		DateRangeEffort another2007 = createYearDateRangeEffort(2007, 2007, 4);
		list3.add(another2007);
		mainList.mergeAdd(list3, projectDateRange);
		assertEquals(twoYears, mainList.getCombinedDateRange());
		assertEquals(7.0, mainList.getTotalUnitQuantity());
		
		DateRangeEffortList list4 = new DateRangeEffortList();
		DateRangeEffort projectTotal = createYearDateRangeEffort(2007, 2009, 8);
		list4.add(projectTotal);
		mainList.mergeAdd(list4, projectDateRange);
		assertEquals(twoYears, mainList.getCombinedDateRange());
		assertEquals(7.0, mainList.getTotalUnitQuantity());

		DateRangeEffortList list5 = new DateRangeEffortList();
		DateRangeEffort month = createMonthDateRangeEffort(2007, 5, 16);
		list5.add(month);
		mainList.mergeAdd(list5, projectDateRange);
		DateRange expectedDateRange = DateRange.combine(dre2006.getDateRange(), month.getDateRange());
		double expectedUnitQuantity = dre2006.getUnitQuantity() + month.getUnitQuantity();
		assertEquals(expectedDateRange, mainList.getCombinedDateRange());
		assertEquals(expectedUnitQuantity, mainList.getTotalUnitQuantity());
	}

	private DateRangeEffort createDateRangeEffort() throws Exception
	{
		int startYear = 1000;
		int endYear = 2000;
		int units = 1;
		return createYearDateRangeEffort(startYear, endYear, units);
	}

	private DateRangeEffort createYearDateRangeEffort(int startYear, int endYear,
			int units) throws Exception
	{
		DateRange dateRange = createYearDateRange(startYear, endYear);
		return new DateRangeEffort("", units, dateRange);
	}

	private DateRange createYearDateRange(int startYear, int endYear)
			throws Exception
	{
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(startYear, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(endYear, 12, 31);
		DateRange dateRange = new DateRange(startDate, endDate);
		return dateRange;
	}
	
	private DateRangeEffort createMonthDateRangeEffort(int year, int month,	int units) throws Exception
	{
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(year, month, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(year, month+1, 1);
		endDate.addDays(-1);
		DateRange dateRange = new DateRange(startDate, endDate);
		
		return new DateRangeEffort("", units, dateRange);
	}
}
