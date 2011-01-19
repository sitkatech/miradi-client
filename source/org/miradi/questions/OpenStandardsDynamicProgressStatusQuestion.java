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

package org.miradi.questions;

import org.miradi.icons.OpenStandardsAutomaticIcon;
import org.miradi.icons.OpenStandardsCompleteIcon;
import org.miradi.icons.OpenStandardsInProgressIcon;
import org.miradi.icons.OpenStandardsNotApplicableIcon;
import org.miradi.icons.OpenStandardsNotStartedIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.project.Project;

public class OpenStandardsDynamicProgressStatusQuestion extends DynamicChoiceQuestion
{
	public OpenStandardsDynamicProgressStatusQuestion(Project projectToUse, String thirdLevelCodeToUse)
	{
		project = projectToUse;
		thirdLevelCode = thirdLevelCodeToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		try
		{
			if (getThirdLevelValue().equals(NOT_STARTED_CODE))
			{
				return new ChoiceItem[] {
						new ChoiceItem(NOT_SPECIFIED_CODE, EAM.text("Automatic"), new OpenStandardsAutomaticIcon()),
						new ChoiceItem(NOT_STARTED_CODE, EAM.text("Not Started"), new OpenStandardsNotStartedIcon()),
						new ChoiceItem(NOT_APPLICABLE_CODE, EAM.text("Not Applicable"), new OpenStandardsNotApplicableIcon()),
				};
			}
			
			return new ChoiceItem[] {
					new ChoiceItem(NOT_SPECIFIED_CODE, EAM.text("Automatic"), new OpenStandardsAutomaticIcon()),
					new ChoiceItem(IN_PROGRESS_CODE, EAM.text("In Progress"), new OpenStandardsInProgressIcon()),
					new ChoiceItem(COMPLETE_CODE, EAM.text("Complete"), new OpenStandardsCompleteIcon()),
					new ChoiceItem(NOT_APPLICABLE_CODE, EAM.text("Not Applicable"), new OpenStandardsNotApplicableIcon()),
			};
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ChoiceItem[0];
		}
	}

	private String getThirdLevelValue() throws Exception
	{
		AbstractStringKeyMap cachedDashboardEffectiveMap = getProject().getCachedDashboardEffectiveMap();
		
		return cachedDashboardEffectiveMap.get(thirdLevelCode);
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	private String thirdLevelCode;
	
	public static final String NOT_SPECIFIED_CODE = "";
	public static final String NOT_STARTED_CODE = "1";
	public static final String IN_PROGRESS_CODE = "2";
	public static final String COMPLETE_CODE = "3";
	public static final String NOT_APPLICABLE_CODE = "4";
}
