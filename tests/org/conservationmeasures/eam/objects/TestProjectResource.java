/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.martus.util.xml.XmlUtilities;

public class TestProjectResource extends TestCaseWithProject
{
	public TestProjectResource(String name)
	{
		super(name);
	}
	
	
	public void testGetResourcesAsHtml() throws Exception
	{
		int resourceCount = 3;
		String expected = "<html>";
		String lessThan = "< ";
		
		ProjectResource[] projectResources = new ProjectResource[resourceCount];
		for (int i = 0; i < projectResources.length; i++)
		{
			projectResources[i] = new ProjectResource(getObjectManager(), new BaseId(i));
			if (i == 0 )
			{
				projectResources[i].setData(ProjectResource.TAG_GIVEN_NAME, lessThan+i);
				expected = expected + XmlUtilities.getXmlEncoded(lessThan)+i;
			}
			else
			{
				projectResources[i].setData(ProjectResource.TAG_GIVEN_NAME, "resource"+i);
				expected = expected + projectResources[i].getGivenName();
			}
			
			if ((i + 1) < projectResources.length)
				expected = expected +"; ";
			
		}
		expected = expected + "</html>";
		
		String resourcesAsHtml = BaseObject.toHtml(projectResources);
		assertEquals("did not return resources as Html?", expected, resourcesAsHtml);
	}
	
	public void testWho() throws Exception
	{
		ProjectResource person = new ProjectResource(getObjectManager(), new BaseId(15));
		assertEquals("(Undefined Resource)", person.getWho());
		String sampleInitials = "rl";
		String sampleGivenName = "Robin";
		String sampleSurName = "Lee";
		String samplePosition = "Manager";
		person.setData(ProjectResource.TAG_POSITION, samplePosition);
		assertEquals(samplePosition, person.getWho());
		person.setData(ProjectResource.TAG_GIVEN_NAME, sampleGivenName);
		assertEquals(sampleGivenName, person.getWho());
		person.setData(ProjectResource.TAG_SUR_NAME, sampleSurName);
		assertEquals(sampleGivenName + " " + sampleSurName, person.getWho());
		person.setData(ProjectResource.TAG_INITIALS, sampleInitials);
		assertEquals(sampleInitials, person.getWho());
	}

	public void testFields() throws Exception
	{
		verifyTagBehavior(ProjectResource.TAG_LABEL);
		verifyTagBehavior(ProjectResource.TAG_INITIALS);
		verifyTagBehavior(ProjectResource.TAG_GIVEN_NAME);
		verifyTagBehavior(ProjectResource.TAG_SUR_NAME);
		verifyTagBehavior(ProjectResource.TAG_POSITION);
	}

	private void verifyTagBehavior(String tag) throws Exception
	{
		String value = "ifislliefj";
		ProjectResource resource = new ProjectResource(getObjectManager(), new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", resource.getData(tag));
		resource.setData(tag, value);
		ProjectResource got = (ProjectResource)ProjectResource.createFromJson(getObjectManager(), resource.getType(), resource.toJson());
		assertEquals(tag + " didn't survive json?", resource.getData(tag), got.getData(tag));
	}
}
