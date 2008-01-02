/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.martus.util.MultiCalendar;

public class TestProjectMetadata extends EAMTestCase
{
	public TestProjectMetadata(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}


	public void testDataFields() throws Exception
	{
		verifyDataField(ProjectMetadata.TAG_PROJECT_NAME, "Gobi Desert Re-humidification");
		verifyDataField(ProjectMetadata.TAG_PROJECT_SCOPE, "Entire Upper Yucatan");
		verifyDataField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "Half of Yucatan");
		verifyDataField(ProjectMetadata.TAG_PROJECT_VISION, "Coral reefs returned to natural state");
		verifyDataField(ProjectMetadata.TAG_SHORT_PROJECT_VISION, "Increase in Sea Life");
		verifyDataField(ProjectMetadata.TAG_START_DATE, "2006-05-22");
		verifyDataField(ProjectMetadata.TAG_EXPECTED_END_DATE, "2006-05-22");
		verifyDataField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, "2006-09-27");
		verifyDataField(ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES, "24.3");
	}
	
	public void testTncDataFields() throws Exception
	{
		verifyDataField(ProjectMetadata.TAG_TNC_LESSONS_LEARNED, "Lessons learned");
		verifyDataField(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER, "WB Version #");
		verifyDataField(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE, "2004-07-19");
		verifyDataField(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, "2006-04-30");
	}
	
	public void testStartDate() throws Exception
	{
		ProjectMetadata info = new ProjectMetadata(new BaseId(63));
		try
		{
			info.setData(ProjectMetadata.TAG_START_DATE, "illegal date");
			fail("Should have thrown for illegal ISO date string");
		}
		catch (InvalidDateException ignoreExpected)
		{
		}
	}
	
	public void testExpectedEndDate() throws Exception
	{
		ProjectMetadata info = new ProjectMetadata(new BaseId(63));
		try
		{
			info.setData(ProjectMetadata.TAG_EXPECTED_END_DATE, "illegal date");
			fail("Should have thrown for illegal ISO date string");
		}
		catch (InvalidDateException ignoreExpected)
		{
		}
	}

	private void verifyDataField(String tag, String data) throws Exception
	{
		ProjectMetadata info = new ProjectMetadata(new BaseId(63));
		assertEquals(tag + " not blank to start?", "", info.getData(tag));
		info.setData(tag, data);
		assertEquals(data, info.getData(tag));
		
		ProjectMetadata got = (ProjectMetadata)ProjectMetadata.createFromJson(project.getObjectManager(), info.getType(), info.toJson());
		assertEquals("Didn't jsonize " + tag + "?", info.getData(tag), got.getData(tag));
	}
	
	public void testGetSkewedMonthFromCode() throws Exception
	{
		verifySkewedMonth(1, "");
		verifySkewedMonth(4, "4");
		verifySkewedMonth(-5, "7");
		verifySkewedMonth(-2, "10");
	}
	
	public void testGetMonthDelta() throws Exception
	{
		verifyMonthDelta(0, 1, 1);
		verifyMonthDelta(3, 1, 4);
		verifyMonthDelta(-6, 1, -5);
		verifyMonthDelta(-3, 1, -2);

		verifyMonthDelta(-3, 4, 1);
		verifyMonthDelta(0, 4, 4);
		verifyMonthDelta(-9, 4, -5);
		verifyMonthDelta(-6, 4, -2);

		verifyMonthDelta(6, -5, 1);
		verifyMonthDelta(9, -5, 4);
		verifyMonthDelta(0, -5, -5);
		verifyMonthDelta(3, -5, -2);

		verifyMonthDelta(3, -2, 1);
		verifyMonthDelta(6, -2, 4);
		verifyMonthDelta(-3, -2, -5);
		verifyMonthDelta(0, -2, -2);

	}

	private void verifyMonthDelta(int expectedDelta, int oldMonth, int newMonth)
	{
		assertEquals(expectedDelta, ProjectMetadata.getMonthDelta(oldMonth, newMonth));
	}

	private void verifySkewedMonth(int expectedMonth, String fiscalYearStartCode)
	{
		assertEquals(expectedMonth, ProjectMetadata.getSkewedMonthFromCode(fiscalYearStartCode));
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

		verifyFiscalQuarterName("2006", "2006-01-01", "2006-12-31", 10);
	}

	
	private void verifyFiscalQuarterName(String expectedName, String beginDate, String endDate, int fiscalYearFirstMonth) throws Exception
	{
		MultiCalendar begin = MultiCalendar.createFromIsoDateString(beginDate);
		MultiCalendar end = MultiCalendar.createFromIsoDateString(endDate);
		DateRange dateRange = new DateRange(begin, end);
		String result = ProjectMetadata.getFiscalYearQuarterName(dateRange, fiscalYearFirstMonth);
		assertEquals(expectedName, result);
	}


	ProjectForTesting project;
}
