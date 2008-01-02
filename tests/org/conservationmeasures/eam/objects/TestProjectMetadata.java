/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.InvalidDateException;

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
	

	ProjectForTesting project;
}
