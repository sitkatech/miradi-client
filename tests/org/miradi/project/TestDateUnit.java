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

public class TestDateUnit extends TestCaseWithProject
{
	public TestDateUnit(String name)
	{
		super(name);
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
		DateRange yearRange = year.asDateRange();
		assertEquals("2006-01-01", yearRange.getStartDate().toIsoDateString());
		assertEquals("2006-12-31", yearRange.getEndDate().toIsoDateString());
		
		DateRange quarterRange = quarter.asDateRange();
		assertEquals("2009-10-01", quarterRange.getStartDate().toIsoDateString());
		assertEquals("2009-12-31", quarterRange.getEndDate().toIsoDateString());
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
	
	public void testGetSubDateUnits() throws Exception
	{
		Vector<DateUnit> yearSubs = year.getSubDateUnits();
		assertEquals(4, yearSubs.size());
		assertEquals(new DateUnit("2006Q1"), yearSubs.get(0));
		assertEquals(new DateUnit("2006Q2"), yearSubs.get(1));
		assertEquals(new DateUnit("2006Q3"), yearSubs.get(2));
		assertEquals(new DateUnit("2006Q4"), yearSubs.get(3));
		
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
		assertEquals(true, year.hasSubDateUnits());
		assertEquals(true, quarter.hasSubDateUnits());
		assertEquals(false, month.hasSubDateUnits());
	}
	
	private final DateUnit empty = new DateUnit("");
	private final DateUnit bogus = new DateUnit("Bogus");
	private final DateUnit year = new DateUnit("2006");
	private final DateUnit quarter = new DateUnit("2009Q4");
	private final DateUnit month = new DateUnit("2008-12");
}
