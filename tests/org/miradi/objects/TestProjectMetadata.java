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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.utils.InvalidDateException;

public class TestProjectMetadata extends TestCaseWithProject
{
	public TestProjectMetadata(String name)
	{
		super(name);
	}
	

	public void testDataFields() throws Exception
	{
		verifyDataField(ProjectMetadata.TAG_PROJECT_NAME, "Gobi Desert Re-humidification");
		verifyDataField(ProjectMetadata.TAG_PROJECT_SCOPE, "Entire Upper Yucatan");
		verifyDataField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "Half of Yucatan");
		verifyDataField(ProjectMetadata.TAG_PROJECT_VISION, "Coral reefs returned to natural state");
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
		ProjectMetadata info = new ProjectMetadata(getObjectManager(), new BaseId(63));
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
		ProjectMetadata info = new ProjectMetadata(getObjectManager(), new BaseId(63));
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
		ProjectMetadata info = new ProjectMetadata(getObjectManager(), new BaseId(63));
		assertEquals(tag + " not blank to start?", "", info.getData(tag));
		info.setData(tag, data);
		assertEquals(data, info.getData(tag));
		
		ProjectMetadata got = (ProjectMetadata)ProjectMetadata.createFromJson(getProject().getObjectManager(), info.getType(), info.toJson());
		assertEquals("Didn't jsonize " + tag + "?", info.getData(tag), got.getData(tag));
	}
}
