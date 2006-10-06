/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestProjectResource extends EAMTestCase
{
	public TestProjectResource(String name)
	{
		super(name);
	}

	public void testFields() throws Exception
	{
		verifyTagBehavior(ProjectResource.TAG_LABEL);
		verifyTagBehavior(ProjectResource.TAG_INITIALS);
		verifyTagBehavior(ProjectResource.TAG_NAME);
		verifyTagBehavior(ProjectResource.TAG_POSITION);
	}

	private void verifyTagBehavior(String tag) throws Exception
	{
		String value = "ifislliefj";
		ProjectResource resource = new ProjectResource(new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", resource.getData(tag));
		resource.setData(tag, value);
		ProjectResource got = (ProjectResource)ProjectResource.createFromJson(resource.getType(), resource.toJson());
		assertEquals(tag + " didn't survive json?", resource.getData(tag), got.getData(tag));
	}
}
