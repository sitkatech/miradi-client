/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsProgressQuestion;
import org.miradi.utils.CodeList;

public class TestDashboard extends ObjectTestCase
{
	public TestDashboard(String name)
	{
		super(name);
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.DASHBOARD);
	}
	
	public void testGetEffectiveStatusMapWithNoData() throws Exception
	{
		 assertEquals("empty dashboard should not have status?",createdEmptyStringChoiceMap() , getEffectiveStatusMap());
	}

	public void testGetEffectiveStatusMapWithData() throws Exception
	{
		verifyProgressCode(OpenStandardsProgressQuestion.NOT_SPECIFIED_CODE);
		
		getProject().createProjectResource();
		verifyProgressCode(OpenStandardsProgressQuestion.IN_PROGRESS_CODE);
	}

	private void verifyProgressCode(String expectedCode) throws Exception
	{
		StringChoiceMap mapAsString = getEffectiveStatusMap();
		StringChoiceMap map = new StringChoiceMap(mapAsString);
		String targetStatusCount = map.get(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE);

		assertEquals("Incorrect status count?", expectedCode, targetStatusCount);
	}
	
	private StringChoiceMap createdEmptyStringChoiceMap()
	{
		CodeList allThirdLEvelCodes = getDashboard().getDashboardRowDefinitionManager().getThirdLevelCodes();
		StringChoiceMap emptyMap = new StringChoiceMap();
		for (int index = 0; index < allThirdLEvelCodes.size(); ++index)
		{
			emptyMap.add(allThirdLEvelCodes.get(index), OpenStandardsProgressQuestion.NOT_SPECIFIED_CODE);
		}
		
		return emptyMap;
	}

	private StringChoiceMap getEffectiveStatusMap() throws Exception
	{
		return getDashboard().getEffectiveStatusMap();
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}
}
