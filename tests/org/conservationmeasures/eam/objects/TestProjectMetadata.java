/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.utils.InvalidDateException;

public class TestProjectMetadata extends EAMTestCase
{
	public TestProjectMetadata(String name)
	{
		super(name);
	}

	public void testDataFields() throws Exception
	{
		verifyDataField(ProjectMetadata.TAG_PROJECT_NAME, "Gobi Desert Re-humidification");
		verifyDataField(ProjectMetadata.TAG_START_DATE, "2006-05-22");
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

	private void verifyDataField(String tag, String data) throws Exception
	{
		ProjectMetadata info = new ProjectMetadata(new BaseId(63));
		assertEquals(tag + " not blank to start?", "", info.getData(tag));
		info.setData(tag, data);
		assertEquals(data, info.getData(tag));
		
		ProjectMetadata got = (ProjectMetadata)ProjectMetadata.createFromJson(info.getType(), info.toJson());
		assertEquals("Didn't jsonize " + tag + "?", info.getData(tag), got.getData(tag));
	}
	
}
