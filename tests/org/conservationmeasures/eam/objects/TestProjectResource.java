/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objectdata.StringData;
import org.martus.util.xml.XmlUtilities;

public class TestProjectResource extends EAMTestCase
{
	public TestProjectResource(String name)
	{
		super(name);
	}
	
	public void testGetResourcesAsHtml()
	{
		int resourceCount = 3;
		String expected = "<html>";
		String lessThan = "< ";
		
		ProjectResource[] projectResources = new ProjectResource[resourceCount];
		for (int i = 0; i < projectResources.length; i++)
		{
			projectResources[i] = new ProjectResource(new BaseId(i));
			if (i == 0 )
			{
				projectResources[i].name = new StringData(lessThan+i);
				expected = expected + XmlUtilities.getXmlEncoded(lessThan)+i;
			}
			else
			{
				projectResources[i].name = new StringData("resource"+i);
				expected = expected + projectResources[i].name;
			}
			
			if ((i + 1) < projectResources.length)
				expected = expected +"; ";
			
		}
		expected = expected + "</html>";
		
		String resourcesAsHtml = EAMBaseObject.toHtml(projectResources);
		assertEquals("did not return resources as Html?", expected, resourcesAsHtml);
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
