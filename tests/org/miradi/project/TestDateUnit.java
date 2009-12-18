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
package org.miradi.project;

import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class TestDateUnit extends TestCaseWithProject
{
	public TestDateUnit(String name)
	{
		super(name);
	}
	
	public void test() throws Exception
	{
		verifyStoreAndRestore(empty);
		verifyStoreAndRestore(fiscalYear2005StartApril);
		verifyStoreAndRestore(quarter4In2009);
		verifyStoreAndRestore(month12);
	}
	
	private void verifyStoreAndRestore(DateUnit dateUnitToVerifyAgainst) throws Exception
	{
		EnhancedJsonObject json = dateUnitToVerifyAgainst.toJson();
		
		DateUnit dateUnitFromJson = new DateUnit(json);
		assertEquals("Json round trip didn't work?", dateUnitToVerifyAgainst, dateUnitFromJson);
	}
		
	public void testCreateFromJson() throws Exception
	{
		verifyCreateFromJson(empty);
		verifyCreateFromJson(fiscalYear2005StartApril);
		verifyCreateFromJson(quarter4In2009);
		verifyCreateFromJson(month12);
	}

	private void verifyCreateFromJson(DateUnit dateUnitToVerifyAgainst) throws Exception
	{
		DateUnit dateUnitFromJson = DateUnit.createFromJson(dateUnitToVerifyAgainst.toJson());
		assertEquals("Create from Json not same date range?", dateUnitToVerifyAgainst, dateUnitFromJson);
			
		assertNull("json from null worked?", DateUnit.createFromJson(null));
		assertNull("Json with no tags worked?", DateUnit.createFromJson(new EnhancedJsonObject()));
		assertNull("Json without expected tag worked?", DateUnit.createFromJson(new EnhancedJsonObject("{bogusTag:\"\"}")));
	}
	
	public void testConstructFromDaterange() throws Exception
	{
		verifyConstruction(fiscalYear2005StartApril);
		verifyConstruction(quarter4In2009);
		verifyConstruction(month12);
	}
	
	private void verifyConstruction(DateUnit dateUnit) throws Exception
	{
		DateRange dateRange = dateUnit.asDateRange();
		DateUnit constructed = DateUnit.createFromDateRange(dateRange);
		assertEquals(dateRange, constructed.asDateRange());
	}

	public void testAsDateRangeForEmpty() throws Exception
	{
		try
		{
			empty.asDateRange();
			fail("Should have thrown for calling asDateRange on empty DateUnit");
		}
		catch(Exception ignoreExpected)
		{
		}
	}
	
	public void testAsDateRangeForBogus() throws Exception
	{
		try
		{
			bogus.asDateRange();
			fail("Should have thrown for calling asDateRange on bogus DateUnit");
		}
		catch(Exception ignoreExpected)
		{
		}
	}
	
	public void testAsDateRange() throws Exception
	{
		verifyDateUnit(fiscalYear2006StartJanuary, "2006-01-01", "2006-12-31");
		verifyDateUnit(fiscalYear2005StartApril, "2005-04-01", "2006-03-31");
		verifyDateUnit(fiscalYear2005StartJuly, "2005-07-01", "2006-06-30");
		verifyDateUnit(fiscalYear2005StartOctober, "2005-10-01", "2006-09-30");
		
		verifyDateUnit(quarter4In2009, "2009-10-01", "2009-12-31");
		
		verifyDateUnit(month01, "2008-01-01", "2008-01-31");
		verifyDateUnit(month02, "2008-02-01", "2008-02-29");
		verifyDateUnit(month03, "2008-03-01", "2008-03-31");
		verifyDateUnit(month04, "2008-04-01", "2008-04-30");
		verifyDateUnit(month05, "2008-05-01", "2008-05-31");
		verifyDateUnit(month06, "2008-06-01", "2008-06-30");
		verifyDateUnit(month07, "2008-07-01", "2008-07-31");
		verifyDateUnit(month08, "2008-08-01", "2008-08-31");
		verifyDateUnit(month09, "2008-09-01", "2008-09-30");
		verifyDateUnit(month10, "2008-10-01", "2008-10-31");
		verifyDateUnit(month11, "2008-11-01", "2008-11-30");
		verifyDateUnit(month12, "2008-12-01", "2008-12-31");
	}

	private void verifyDateUnit(DateUnit dateUnit, String expectedStartDate, String expectedEndDate) throws Exception
	{
		DateRange monthRange = dateUnit.asDateRange();
		assertEquals(expectedStartDate, monthRange.getStartDate().toIsoDateString());
		assertEquals(expectedEndDate, monthRange.getEndDate().toIsoDateString());
	}
	
	public void testGetSubDateUnitsForEmpty() throws Exception
	{
		try
		{
			empty.getSubDateUnits();
			fail("Should have thrown for calling getSubDateUnits on empty DateUnit");
		}
		catch(Exception ignoreExpected)
		{
		}
	}
	
	public void testGetSubDateUnitsForBogus() throws Exception
	{
		try
		{
			bogus.asDateRange();
			fail("Should have thrown for calling getSubDateUnits on bogus DateUnit");
		}
		catch(Exception ignoreExpected)
		{
		}
	}
	
	public void testGetSubDateUnitsForCalendarYear() throws Exception
	{
		Vector<DateUnit> yearSubs = fiscalYear2006StartJanuary.getSubDateUnits();
		String[] expected = {"2006Q1", "2006Q2", "2006Q3", "2006Q4", };
		verifyDateUnits(expected, yearSubs);
	}
	
	private void verifyDateUnits(String[] expectedCodes, Vector<DateUnit>gotDateUnits)
	{
		assertEquals(expectedCodes.length, gotDateUnits.size());
		for(int i = 0; i < expectedCodes.length; ++i)
			assertEquals(new DateUnit(expectedCodes[i]), gotDateUnits.get(i));
	}
	
	public void testGetSubDateUnitsForFiscalYear() throws Exception
	{
		verifyDateUnits(new String[] {"2005Q2", "2005Q3", "2005Q4", "2006Q1", }, fiscalYear2005StartApril.getSubDateUnits());
		verifyDateUnits(new String[] {"2005Q3", "2005Q4", "2006Q1", "2006Q2", }, fiscalYear2005StartJuly.getSubDateUnits());
		verifyDateUnits(new String[] {"2005Q4", "2006Q1", "2006Q2", "2006Q3", }, fiscalYear2005StartOctober.getSubDateUnits());
	}
	
	public void testGetSubDateUnitsForQuarter() throws Exception
	{
		Vector<DateUnit> quarterSubs = quarter4In2009.getSubDateUnits();
		assertEquals(3, quarterSubs.size());
		assertEquals(new DateUnit("2009-10"), quarterSubs.get(0));
		assertEquals(new DateUnit("2009-11"), quarterSubs.get(1));
		assertEquals(new DateUnit("2009-12"), quarterSubs.get(2));
	}
	
	public void testGetSubDateUnitsForMonth() throws Exception
	{
		verifyMonthSubDateUnits(month01, 31);
		verifyMonthSubDateUnits(month02, 29);
		verifyMonthSubDateUnits(month03, 31);
		verifyMonthSubDateUnits(month04, 30);
		verifyMonthSubDateUnits(month05, 31);
		verifyMonthSubDateUnits(month06, 30);
		verifyMonthSubDateUnits(month07, 31);
		verifyMonthSubDateUnits(month08, 31);
		verifyMonthSubDateUnits(month09, 30);
		verifyMonthSubDateUnits(month10, 31);
		verifyMonthSubDateUnits(month11, 30);
		verifyMonthSubDateUnits(month12, 31);
	}

	private void verifyMonthSubDateUnits(DateUnit monthToVerify, final int expectedDayCountInMonth) throws Exception
	{
		Vector<DateUnit> monthSubs = monthToVerify.getSubDateUnits();
		assertEquals("wrong days in month count?", expectedDayCountInMonth, monthSubs.size());
		for (int index = 0; index < (expectedDayCountInMonth); ++index)
		{
			int day = index + 1;
			String expectedDay = monthToVerify.toString() + "-" + DateUnit.asTwoDigitString(day);
			assertEquals("wrong day ", monthSubs.get(index).toString(), expectedDay);
		}
	}
	
	public void testGetBoundedSubDateUnitsForTotal() throws Exception
	{
		verifyTotalSubDateUnits(create2006DateRange(), 1);
		verifyTotalSubDateUnits(create2005To2006DateRange(), 2);
		verifyTotalSubDateUnits(createOneDayDateRange(), 1);
	}

	private void verifyTotalSubDateUnits(DateRange bounds, int expectedYearCount) throws Exception
	{
		Vector<DateUnit> totalSubDateUnits = getProject().getProjectCalendar().getSubDateUnits(bounds, empty);
		assertEquals("wrong sub dateUnits count?", expectedYearCount, totalSubDateUnits.size());
	}
	
	public void testGetBoundedSubDateUnitsForYear() throws Exception
	{
		DateRange bounds = create2006DateRange();
		Vector<DateUnit> yearSubDateUnits = fiscalYear2006StartJanuary.getSubDateUnits(bounds);
		assertEquals("wrong sub dateUnits count?", 4, yearSubDateUnits.size());
		for(int index = 0; index < yearSubDateUnits.size(); ++index)
		{
			DateUnit quarterDateUnit = yearSubDateUnits.get(index);
			int quarter = index + 1;
			assertEquals("wrong sub dateUnit for year?", new DateUnit("2006Q" + quarter), quarterDateUnit);
		}
	}
	
	public void testBoundedSubDateUnitsForPartialYear() throws Exception
	{
		DateRange bounds = createOneDayDateRange();
		Vector<DateUnit> yearSubDateUnits = fiscalYear2006StartJanuary.getSubDateUnits(bounds);
		assertEquals("wrong sub dateUnits count?", 1, yearSubDateUnits.size());
		
		Vector<DateUnit> subDateUnitsOutSideBounds = new DateUnit("YEARFROM:2001-01").getSubDateUnits(bounds);
		assertEquals("no sub dateUnits for dateUnit outside of bounds", 0, subDateUnitsOutSideBounds.size());
	}

	public void testGetBoundedSubDateUnitsForQuarter() throws Exception
	{
		DateRange oneYearBound = create2006DateRange();
		Vector<DateUnit> quarter1SubDateUnits = quarter1In2006.getSubDateUnits(oneYearBound);
		assertEquals("wrong quarter 1 sub dateUnit months count", 3, quarter1SubDateUnits.size());
		
		DateRange oneDayBound = createOneDayDateRange();
		Vector<DateUnit> quarter3SubDateUnits = new DateUnit("2006Q3").getSubDateUnits(oneDayBound);
		assertEquals("wrong quarter 3 sub dateUnit months count", 1, quarter3SubDateUnits.size());
		DateUnit singleMonth = quarter3SubDateUnits.get(0);
		assertEquals("wrong quarter sub dateUnit month?", singleMonth, new DateUnit("2006-08"));
		
		Vector<DateUnit> subDateUnitsOutSideBounds = new DateUnit("2001Q1").getSubDateUnits(oneYearBound);
		assertEquals("no sub dateUnits for dateUnit outside of bounds", 0, subDateUnitsOutSideBounds.size());
	}
	
	public void testGetBoundedSubDateUnitsForMonth() throws Exception
	{
		DateRange oneYearBound = create2006DateRange();
		Vector<DateUnit> janSubDateUnits = new DateUnit("2006-01").getSubDateUnits(oneYearBound);
		assertEquals("wrong jan sub dateUnits days count?", 31, janSubDateUnits.size());
		
		DateRange oneDayBound = createOneDayDateRange();
		Vector<DateUnit> augSubDateUnits = new DateUnit("2006-08").getSubDateUnits(oneDayBound);
		assertEquals("wrong august sub dateUnits days count", 1, augSubDateUnits.size());
		
		Vector<DateUnit> subDateUnitsOutSideBounds = new DateUnit("2002-10").getSubDateUnits(oneDayBound);
		assertEquals("no sub dateUnits for dateUnit outside of bounds", 0, subDateUnitsOutSideBounds.size());
	}
	

	private DateRange create2006DateRange() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2006, 1, 1);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2006, 12, 31);
		
		return new DateRange(start, end);
	}
	
	private DateRange create2005To2006DateRange() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2005, 8, 1);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2006, 4, 13);
		
		return new DateRange(start, end);
	}
	
	private DateRange createOneDayDateRange() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2006, 8, 5);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2006, 8, 5);
		
		return new DateRange(start, end);
	}
	
	public void testHasSubDateUnits() throws Exception
	{
		assertEquals(false, empty.hasSubDateUnits());
		assertEquals(false, bogus.hasSubDateUnits());
		assertEquals(true, fiscalYear2006StartJanuary.hasSubDateUnits());
		assertEquals(true, quarter4In2009.hasSubDateUnits());
		assertEquals(true, month12.hasSubDateUnits());
	}
	
	public void testGetDay()
	{
		verifyMonthDay(singleRandomDay1, 12, 10);
		verifyMonthDay(new DateUnit("2008-03-13"), 3, 13);
		verifyMonthDay(new DateUnit("2008-01-05"), 1, 5);
		verifyMonthDay(new DateUnit("2008-02-01"), 2, 1);
	}
	
	public void verifyMonthDay(DateUnit dateUnit, int expectedMonth, int expectedDay)
	{
		assertEquals("wrong month", expectedMonth, dateUnit.getMonth());
		assertEquals("wrong day?", expectedDay, dateUnit.getDay());
	}
	
	public void testToString()
	{
		assertEquals("wrong toString value?", "2008-12-10", singleRandomDay1.toString());
	}
	
	public void testGetSuperDateUnit() throws Exception
	{
		assertEquals(new DateUnit("2005-07"), new DateUnit("2005-07-15").getSuperDateUnit(1));
		assertEquals(new DateUnit("2005Q3"), new DateUnit("2005-07").getSuperDateUnit(1));
		assertEquals(new DateUnit("YEARFROM:2005-01"), new DateUnit("2005Q3").getSuperDateUnit(1));
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-01").getSuperDateUnit(1));
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-04").getSuperDateUnit(4));
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-07").getSuperDateUnit(7));
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-10").getSuperDateUnit(10));
		
		try
		{
			new DateUnit("abc").getSuperDateUnit(1);
			fail("Should have thrown for bogus date unit");
		}
		catch(RuntimeException ignoreExpected)
		{
		}
	}
	
	public void testGetSuperDateUnitForFyQuarter() throws Exception
	{
		assertEquals("wrong year?", "YEARFROM:2009-01", quarter1In2009.getSuperDateUnit(1).toString());
		assertEquals("wrong year?", "YEARFROM:2008-04", quarter1In2009.getSuperDateUnit(4).toString());
		assertEquals("wrong year?", "YEARFROM:2008-07", quarter1In2009.getSuperDateUnit(7).toString());
		assertEquals("wrong year?", "YEARFROM:2008-10", quarter1In2009.getSuperDateUnit(10).toString());
		
		assertEquals("wrong year?", "YEARFROM:2009-01", quarter2In2009.getSuperDateUnit(1).toString());
		assertEquals("wrong year?", "YEARFROM:2009-04", quarter2In2009.getSuperDateUnit(4).toString());
		assertEquals("wrong year?", "YEARFROM:2008-07", quarter2In2009.getSuperDateUnit(7).toString());
		assertEquals("wrong year?", "YEARFROM:2008-10", quarter2In2009.getSuperDateUnit(10).toString());
		
		assertEquals("wrong year?", "YEARFROM:2009-01", quarter3In2009.getSuperDateUnit(1).toString());
		assertEquals("wrong year?", "YEARFROM:2009-04", quarter3In2009.getSuperDateUnit(4).toString());
		assertEquals("wrong year?", "YEARFROM:2009-07", quarter3In2009.getSuperDateUnit(7).toString());
		assertEquals("wrong year?", "YEARFROM:2008-10", quarter3In2009.getSuperDateUnit(10).toString());
		
		assertEquals("wrong year?", "YEARFROM:2009-01", quarter4In2009.getSuperDateUnit(1).toString());
		assertEquals("wrong year?", "YEARFROM:2009-04", quarter4In2009.getSuperDateUnit(4).toString());
		assertEquals("wrong year?", "YEARFROM:2009-07", quarter4In2009.getSuperDateUnit(7).toString());
		assertEquals("wrong year?", "YEARFROM:2009-10", quarter4In2009.getSuperDateUnit(10).toString());
	}
	
	public void testContains() throws Exception
	{
		DateUnit firstDayOfJan2006 = new DateUnit("2006-01-01");
		DateUnit month01In2006 = new DateUnit("2006-01");
		
		verifyContains(empty, empty);
		verifyContains(empty, fiscalYear2006StartJanuary);
		verifyContains(empty, fiscalYear2005StartApril);
		verifyContains(empty, fiscalYear2005StartJuly);
		verifyContains(empty, fiscalYear2005StartOctober);
		verifyContains(empty, quarter4In2009);
		verifyContains(empty, month01);
		verifyContains(empty, singleRandomDay1);
		
		verifyContains(fiscalYear2006StartJanuary, fiscalYear2006StartJanuary);
		verifyContains(fiscalYear2006StartJanuary, quarter1In2006);
		verifyContains(fiscalYear2006StartJanuary, month01In2006);
		verifyContains(fiscalYear2006StartJanuary, firstDayOfJan2006);
		
		verifyNotContains(fiscalYear2006StartJanuary, new DateUnit("2005-05-01"));
		verifyNotContains(fiscalYear2006StartJanuary, month04);
		verifyNotContains(fiscalYear2006StartJanuary, quarter4In2009);
		verifyNotContains(fiscalYear2006StartJanuary, fiscalYear2005StartApril);
		verifyNotContains(fiscalYear2006StartJanuary, empty);
		
		verifyContains(quarter4In2009, quarter4In2009);
		verifyContains(quarter4In2009, new DateUnit("2009-12"));
		verifyContains(quarter4In2009, new DateUnit("2009-11-01"));
		verifyNotContains(quarter4In2009, firstDayOfJan2006);
		verifyNotContains(quarter4In2009, month04);
		verifyNotContains(quarter4In2009, quarter1In2009);
		verifyNotContains(quarter4In2009, fiscalYear2005StartOctober);
		verifyNotContains(quarter4In2009, empty);
		
		verifyContains(month01, month01);
		verifyContains(month01In2006, firstDayOfJan2006);
		
		verifyNotContains(month01, month02);
		verifyNotContains(month01, quarter1In2009);
		verifyNotContains(month01, fiscalYear2006StartJanuary);
		verifyNotContains(month01, empty);
	
		verifyContains(singleRandomDay1, singleRandomDay1);
		verifyNotContains(singleRandomDay1, firstDayOfJan2006);
		verifyNotContains(singleRandomDay1, month01);
		verifyNotContains(singleRandomDay1, quarter1In2009);
		verifyNotContains(singleRandomDay1, fiscalYear2005StartApril);
		verifyNotContains(singleRandomDay1, empty);
	}
	
	private void verifyContains(DateUnit outer, DateUnit inner) throws Exception
	{
		assertTrue(outer + " didn't contain " + inner + "?", outer.contains(inner));
	}
	
	private void verifyNotContains(DateUnit outer, DateUnit inner) throws Exception
	{
		 assertFalse(outer + " did contain " + inner + "?", outer.contains(inner));
	}
	
	private final DateUnit empty = new DateUnit("");
	private final DateUnit bogus = new DateUnit("Bogus");
	private final DateUnit fiscalYear2006StartJanuary = new DateUnit("YEARFROM:2006-01");
	private final DateUnit fiscalYear2005StartApril = new DateUnit("YEARFROM:2005-04");
	private final DateUnit fiscalYear2005StartJuly = new DateUnit("YEARFROM:2005-07");
	private final DateUnit fiscalYear2005StartOctober = new DateUnit("YEARFROM:2005-10");
	private final DateUnit quarter1In2009 = new DateUnit("2009Q1");
	private final DateUnit quarter2In2009 = new DateUnit("2009Q2");
	private final DateUnit quarter3In2009 = new DateUnit("2009Q3");
	private final DateUnit quarter4In2009 = new DateUnit("2009Q4");
	
	private final DateUnit quarter1In2006 = new DateUnit("2006Q1");
	
	public static final DateUnit month01 = new DateUnit("2008-01");
	private final DateUnit month02 = new DateUnit("2008-02");
	private final DateUnit month03 = new DateUnit("2008-03");
	private final DateUnit month04 = new DateUnit("2008-04");
	private final DateUnit month05 = new DateUnit("2008-05");
	private final DateUnit month06 = new DateUnit("2008-06");
	private final DateUnit month07 = new DateUnit("2008-07");
	private final DateUnit month08 = new DateUnit("2008-08");
	private final DateUnit month09 = new DateUnit("2008-09");
	private final DateUnit month10 = new DateUnit("2008-10");
	private final DateUnit month11 = new DateUnit("2008-11");
	public static final DateUnit month12 = new DateUnit("2008-12");
	
	private final DateUnit singleRandomDay1 = new DateUnit("2008-12-10");
}
