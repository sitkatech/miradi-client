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

import org.martus.util.xml.XmlUtilities;
import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.utils.CodeList;

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
	
	public void testIsTeamLead() throws Exception
	{
		ProjectResource resource = new ProjectResource(getObjectManager(), new BaseId(22));
		assertFalse("is not team lead?", resource.isTeamLead());
		
		CodeList roleCodes = new CodeList();
		roleCodes.add(ResourceRoleQuestion.TeamLeaderCode);
		
		resource.setData(ProjectResource.TAG_ROLE_CODES, roleCodes.toString());
		assertTrue("is team lead?", resource.isTeamLead());	
	}
}
