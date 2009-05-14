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
		verifyStoreAndRestore(fiscalYearStartApril);
		verifyStoreAndRestore(quarter);
		verifyStoreAndRestore(month);
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
		verifyCreateFromJson(fiscalYearStartApril);
		verifyCreateFromJson(quarter);
		verifyCreateFromJson(month);
	}

	private void verifyCreateFromJson(DateUnit dateUnitToVerifyAgainst) throws Exception
	{
		DateUnit dateUnitFromJson = DateUnit.createFromJson(dateUnitToVerifyAgainst.toJson());
		assertEquals("not same date unit?", dateUnitToVerifyAgainst, dateUnitFromJson);
			
		assertNull("not null?", DateRange.createFromJson(null));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject()));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject("{bogusTag:\"\"}")));
	}
	
	public void testConstructFromDaterange() throws Exception
	{
		verifyConstruction(fiscalYearStartApril);
		verifyConstruction(quarter);
		verifyConstruction(month);
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
		verifyDateUnit(fiscalYearStartJanuary, "2006-01-01", "2006-12-31");
		verifyDateUnit(fiscalYearStartApril, "2005-04-01", "2006-03-31");
		verifyDateUnit(fiscalYearStartJuly, "2005-07-01", "2006-06-30");
		verifyDateUnit(fiscalYearStartOctober, "2005-10-01", "2006-09-30");
		
		verifyDateUnit(quarter, "2009-10-01", "2009-12-31");
		
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
		Vector<DateUnit> yearSubs = fiscalYearStartJanuary.getSubDateUnits();
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
		verifyDateUnits(new String[] {"2005Q2", "2005Q3", "2005Q4", "2006Q1", }, fiscalYearStartApril.getSubDateUnits());
		verifyDateUnits(new String[] {"2005Q3", "2005Q4", "2006Q1", "2006Q2", }, fiscalYearStartJuly.getSubDateUnits());
		verifyDateUnits(new String[] {"2005Q4", "2006Q1", "2006Q2", "2006Q3", }, fiscalYearStartOctober.getSubDateUnits());
	}
	
	public void testGetSubDateUnitsForQuarter() throws Exception
	{
		Vector<DateUnit> quarterSubs = quarter.getSubDateUnits();
		assertEquals(3, quarterSubs.size());
		assertEquals(new DateUnit("2009-10"), quarterSubs.get(0));
		assertEquals(new DateUnit("2009-11"), quarterSubs.get(1));
		assertEquals(new DateUnit("2009-12"), quarterSubs.get(2));
		
	}
	
	public void testHasSubDateUnits() throws Exception
	{
		assertEquals(false, empty.hasSubDateUnits());
		assertEquals(false, bogus.hasSubDateUnits());
		assertEquals(true, fiscalYearStartJanuary.hasSubDateUnits());
		assertEquals(true, quarter.hasSubDateUnits());
		assertEquals(true, month.hasSubDateUnits());
	}
	
	public void testGetDay()
	{
		verifyMonthDay(new DateUnit("2008-12-10"), 12, 10);
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
		DateUnit dateUnit = new DateUnit("2008-12-10");
		assertEquals("wrong toString value?", "2008-12-10", dateUnit.toString());
	}
	
	public void testGetSuperDateUnit() throws Exception
	{
		assertEquals(new DateUnit("2005-07"), new DateUnit("2005-07-15").getSuperDateUnit());
		assertEquals(new DateUnit("2005Q3"), new DateUnit("2005-07").getSuperDateUnit());
		assertEquals(new DateUnit("YEARFROM:2005-01"), new DateUnit("2005Q3").getSuperDateUnit());
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-01").getSuperDateUnit());
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-04").getSuperDateUnit());
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-07").getSuperDateUnit());
		assertEquals(new DateUnit(""), new DateUnit("YEARFROM:2005-10").getSuperDateUnit());
		
		try
		{
			new DateUnit("abc").getSuperDateUnit();
			fail("Should have thrown for bogus date unit");
		}
		catch(RuntimeException ignoreExpected)
		{
		}
	}
	
	private final DateUnit empty = new DateUnit("");
	private final DateUnit bogus = new DateUnit("Bogus");
	private final DateUnit fiscalYearStartJanuary = new DateUnit("YEARFROM:2006-01");
	private final DateUnit fiscalYearStartApril = new DateUnit("YEARFROM:2005-04");
	private final DateUnit fiscalYearStartJuly = new DateUnit("YEARFROM:2005-07");
	private final DateUnit fiscalYearStartOctober = new DateUnit("YEARFROM:2005-10");
	private final DateUnit quarter = new DateUnit("2009Q4");
	
	public static final DateUnit month = new DateUnit("2008-12");
	
	private final DateUnit month01 = new DateUnit("2008-01");
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
	private final DateUnit month12 = new DateUnit("2008-12");
}
