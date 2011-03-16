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

import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsDynamicProgressStatusQuestion;
import org.miradi.questions.OpenStandardsImplementActionsAndMonitoringQuestion;
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

	@Override
	protected BaseObject createObject(int objectType, CreateObjectParameter extraInfo) throws Exception
	{
		if (Dashboard.is(objectType))
		{
			ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
			return Dashboard.find(getProject(), dashboardRef);
		}
		
		return super.createObject(objectType, extraInfo);
	}
	
	public void testGetEffectiveStatusMapWithNoData() throws Exception
	{
		StringChoiceMap createEmptyStringChoiceMap = createStringChoiceMapForEmptyProject();
		StringChoiceMap effectiveStatusMap = getEffectiveStatusMap();
		
		assertEquals("empty dashboard should not have status?",createEmptyStringChoiceMap , effectiveStatusMap);
	}

	public void testGetEffectiveStatusMapWithData() throws Exception
	{
		verifyTeamMemberEffectiveStatus(OpenStandardsDynamicProgressStatusQuestion.NOT_STARTED_CODE);
		
		getProject().createProjectResource();
		verifyTeamMemberEffectiveStatus(OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE);
	}
	
	public void testGetEffectiveStatusMapWithOneOfTwoRowsHavingData() throws Exception
	{
		verifyEffectiveStatus(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE, OpenStandardsDynamicProgressStatusQuestion.NOT_STARTED_CODE);
		
		getProject().createThreat();
		verifyEffectiveStatus(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE, OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE);
	}
	
	public void testGetEffectiveStatusMapWithUserData() throws Exception
	{
		getProject().createProjectResource();
		StringChoiceMap userMap = new StringChoiceMap();
		userMap.put(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE, OpenStandardsDynamicProgressStatusQuestion.COMPLETE_CODE);
		getDashboard().setData(Dashboard.TAG_PROGRESS_CHOICE_MAP, userMap.toString());
		verifyTeamMemberEffectiveStatus(OpenStandardsDynamicProgressStatusQuestion.COMPLETE_CODE);
	}

	private void verifyTeamMemberEffectiveStatus(String expectedCode) throws Exception
	{
		verifyEffectiveStatus(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE, expectedCode);
	}

	private void verifyEffectiveStatus(final String thirdLevelRowCode, String expectedCode) throws Exception
	{
		StringChoiceMap mapAsString = getEffectiveStatusMap();
		StringChoiceMap map = new StringChoiceMap(mapAsString);
		String calculatedStatusCode = map.get(thirdLevelRowCode);

		assertEquals("Incorrect status code?", expectedCode, calculatedStatusCode);
	}
	
	private StringChoiceMap createStringChoiceMapForEmptyProject()
	{
		CodeList allThirdLEvelCodes = getDashboard().getDashboardRowDefinitionManager().getThirdLevelCodes();
		StringChoiceMap emptyMap = new StringChoiceMap();
		for (int index = 0; index < allThirdLEvelCodes.size(); ++index)
		{
			String progressCode = OpenStandardsDynamicProgressStatusQuestion.NOT_STARTED_CODE;
			String thirdLevelCode = allThirdLEvelCodes.get(index);
			if (thirdLevelCode.equals(OpenStandardsImplementActionsAndMonitoringQuestion.ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE))
				progressCode = OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE;
			
			if (thirdLevelCode.equals(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE))
				progressCode = OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE;
			
			emptyMap.put(thirdLevelCode, progressCode);
		}
		
		return emptyMap;
	}

	private StringChoiceMap getEffectiveStatusMap() throws Exception
	{
		return getDashboard().calculateEffectiveStatusMap();
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}
}
