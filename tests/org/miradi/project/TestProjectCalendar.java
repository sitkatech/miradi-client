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
import org.miradi.objects.ProjectMetadata;
import org.miradi.utils.DateRange;

public class TestProjectCalendar extends TestCaseWithProject
{
	public TestProjectCalendar(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		ProjectCalendar pc = getProjectCalendar();
		getProject().getMetadata().setData(ProjectMetadata.TAG_START_DATE, "2006-01-01");
		getProject().getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, "2007-12-31");
		pc.rebuildProjectDateRanges();
		{
			Vector yearlyRanges = pc.getYearlyDateRanges();
			assertEquals(2, yearlyRanges.size());
			assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
			assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
		}

		getProject().getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, "7");
		pc.rebuildProjectDateRanges();
		{
			Vector yearlyRanges = pc.getYearlyDateRanges();
			assertEquals(3, yearlyRanges.size());
			assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
			assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
			assertEquals("FY08", pc.getDateRangeName((DateRange)yearlyRanges.get(2)));
		}

		getProject().getMetadata().setData(ProjectMetadata.TAG_START_DATE, "2006-01-02");
		getProject().getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, "2007-01-02");
		getProject().getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, "1");
		pc.rebuildProjectDateRanges();
		{
			Vector yearlyRanges = pc.getYearlyDateRanges();
			assertEquals(2, yearlyRanges.size());
			assertEquals("FY06", pc.getDateRangeName((DateRange)yearlyRanges.get(0)));
			assertEquals("FY07", pc.getDateRangeName((DateRange)yearlyRanges.get(1)));
		}
	}

	public void testGetFiscalYearQuarterName() throws Exception
	{
		verifyFiscalQuarterName("FY06", "2006-01-01", "2006-12-31", 1);
		verifyFiscalQuarterName("Q1 FY06", "2006-01-01", "2006-03-31", 1);
		verifyFiscalQuarterName("Q2 FY06", "2006-04-01", "2006-06-30", 1);
		verifyFiscalQuarterName("Q3 FY06", "2006-07-01", "2006-09-30", 1);
		verifyFiscalQuarterName("Q4 FY06", "2006-10-01", "2006-12-31", 1);
		
		verifyFiscalQuarterName("FY06", "2006-04-01", "2007-03-31", 4);
		verifyFiscalQuarterName("Q1 FY06", "2006-04-01", "2006-06-30", 4);
		verifyFiscalQuarterName("Q2 FY06", "2006-07-01", "2006-09-30", 4);
		verifyFiscalQuarterName("Q3 FY06", "2006-10-01", "2006-12-31", 4);
		verifyFiscalQuarterName("Q4 FY06", "2007-01-01", "2007-03-31", 4);

		verifyFiscalQuarterName("FY06", "2005-07-01", "2006-06-30", 7);
		verifyFiscalQuarterName("Q1 FY06", "2005-07-01", "2005-09-30", 7);
		verifyFiscalQuarterName("Q2 FY06", "2005-10-01", "2005-12-31", 7);
		verifyFiscalQuarterName("Q3 FY06", "2006-01-01", "2006-03-31", 7);
		verifyFiscalQuarterName("Q4 FY06", "2006-04-01", "2006-06-30", 7);

		verifyFiscalQuarterName("FY06", "2005-10-01", "2006-09-30", 10);
		verifyFiscalQuarterName("Q1 FY06", "2005-10-01", "2005-12-31", 10);
		verifyFiscalQuarterName("Q2 FY06", "2006-01-01", "2006-03-31", 10);
		verifyFiscalQuarterName("Q3 FY06", "2006-04-01", "2006-06-30", 10);
		verifyFiscalQuarterName("Q4 FY06", "2006-07-01", "2006-09-30", 10);

		verifyFiscalQuarterName("Q1 FY06 - Q3 FY06", "2005-07-01", "2006-03-31", 7);
		verifyFiscalQuarterName("Q4 FY06 - Q1 FY07", "2006-04-01", "2006-09-30", 7);

		verifyFiscalQuarterName("2006", "2006-01-01", "2006-12-31", 10);
	}

	
	private void verifyFiscalQuarterName(String expectedName, String beginDate, String endDate, int fiscalYearFirstMonth) throws Exception
	{
		MultiCalendar begin = getProject().parseIsoDate(beginDate);
		MultiCalendar end = getProject().parseIsoDate(endDate);
		DateRange dateRange = new DateRange(begin, end);
		String result = ProjectCalendar.getFiscalYearQuarterName(dateRange, fiscalYearFirstMonth);
		assertEquals(expectedName, result);
	}
	
	public void testConvertToDateRange() throws Exception
	{
		verifyBlankDateUnit();
		verifyWithBadDateData();
		verifyWithYearDateUnit();
	}

	private void verifyWithYearDateUnit() throws Exception
	{
		DateUnit dateUnit = new DateUnit("2006");
		DateRange dateRange = getProjectCalendar().convertToDateRange(dateUnit);
		assertEquals("wrong start date?", "2006-01-01", dateRange.getStartDate().toIsoDateString());
		assertEquals("wrong end date?", "2006-12-31", dateRange.getEndDate().toIsoDateString());
	}

	private void verifyBlankDateUnit() throws Exception
	{
		MultiCalendar startDate = getProject().parseIsoDate("2006-01-02");
		MultiCalendar endDate = getProject().parseIsoDate("2007-01-02");
		
		getProject().getMetadata().setData(ProjectMetadata.TAG_START_DATE, startDate.toIsoDateString());
		getProject().getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, endDate.toIsoDateString());
		DateUnit blankDateUnit = new DateUnit();
		DateRange dateRange = getProjectCalendar().convertToDateRange(blankDateUnit);
		DateRange expectedDateRange = new DateRange(startDate, endDate);
		assertEquals("date ranges do not match?", expectedDateRange, dateRange);
	}

	private void verifyWithBadDateData()
	{
		try
		{
			DateUnit bogusDateUnit = new DateUnit("bogusDate");
			getProjectCalendar().convertToDateRange(bogusDateUnit);
			fail("should have thrown an exception when trying to convert invalid data?");
		}
		catch (Exception ignoreExpected)
		{
		}
	}
	
	public void testGetSubDateUnits() throws Exception
	{
		MultiCalendar startDate = getProject().parseIsoDate("2006-01-02");
		MultiCalendar endDate = getProject().parseIsoDate("2007-01-02");
		
		getProject().getMetadata().setData(ProjectMetadata.TAG_START_DATE, startDate.toIsoDateString());
		getProject().getMetadata().setData(ProjectMetadata.TAG_EXPECTED_END_DATE, endDate.toIsoDateString());
		
		DateUnit blankDateUnit = new DateUnit();
		Vector<DateUnit> subDateUnits = getProjectCalendar().getSubDateUnits(blankDateUnit);
		assertEquals("wrong sub date units count?", 2, subDateUnits.size());
		assertTrue("does not contain date?", subDateUnits.contains(new DateUnit("2006")));
		assertTrue("does not contain date?", subDateUnits.contains(new DateUnit("2007")));
	}

	private ProjectCalendar getProjectCalendar()
	{
		return getProject().getProjectCalendar();
	}
}
