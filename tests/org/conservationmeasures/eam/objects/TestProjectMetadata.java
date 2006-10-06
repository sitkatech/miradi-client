/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestProjectMetadata extends EAMTestCase
{
	public TestProjectMetadata(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		String projectName = "Gobi Desert Re-humidification";
		ProjectMetadata info = new ProjectMetadata(new BaseId(63));
		assertEquals("Name not blank to start?", "", info.getData(ProjectMetadata.TAG_PROJECT_NAME));
		info.setData(ProjectMetadata.TAG_PROJECT_NAME, projectName);
		assertEquals(projectName, info.getData(ProjectMetadata.TAG_PROJECT_NAME));
		
		ProjectMetadata got = (ProjectMetadata)ProjectMetadata.createFromJson(info.getType(), info.toJson());
		assertEquals("Didn't jsonize name?", info.getData(ProjectMetadata.TAG_PROJECT_NAME), got.getData(ProjectMetadata.TAG_PROJECT_NAME));
	}
}
