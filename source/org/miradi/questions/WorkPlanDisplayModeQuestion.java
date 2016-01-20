/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.icons.IconManager;
import org.miradi.main.EAM;

import javax.swing.*;

public class WorkPlanDisplayModeQuestion extends StaticChoiceQuestion
{
	public WorkPlanDisplayModeQuestion()
	{
		super(getStaticChoices());
	}

	static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem(SHOW_BOTH_WORK_PLANS_CODE, getTextForChoice(SHOW_BOTH_WORK_PLANS_CODE)),
			new ChoiceItem(SHOW_ONLY_SHARED_WORK_PLAN_CODE, getTextForChoice(SHOW_ONLY_SHARED_WORK_PLAN_CODE), getIconForChoice(SHOW_ONLY_SHARED_WORK_PLAN_CODE)),
			new ChoiceItem(SHOW_ONLY_LEGACY_WORK_PLAN_CODE, getTextForChoice(SHOW_ONLY_LEGACY_WORK_PLAN_CODE), getIconForChoice(SHOW_ONLY_LEGACY_WORK_PLAN_CODE)),
		};
	}

	public static String getTextForChoice(String choice)
	{
		if (choice.equals(WorkPlanDisplayModeQuestion.SHOW_BOTH_WORK_PLANS_CODE))
			return EAM.text("Shared Work Plan and legacy Work Plan");

		if (choice.equals(WorkPlanDisplayModeQuestion.SHOW_ONLY_SHARED_WORK_PLAN_CODE))
			return EAM.text("Only Shared Work Plan");

		if (choice.equals(WorkPlanDisplayModeQuestion.SHOW_ONLY_LEGACY_WORK_PLAN_CODE))
			return EAM.text("Only legacy Work Plan");

		return "";
	}

	public static Icon getIconForChoice(String choice)
	{
		if (choice.equals(WorkPlanDisplayModeQuestion.SHOW_ONLY_SHARED_WORK_PLAN_CODE))
			return IconManager.getSharedWorkPlanIcon();

		if (choice.equals(WorkPlanDisplayModeQuestion.SHOW_ONLY_LEGACY_WORK_PLAN_CODE))
			return IconManager.getWorkPlanIcon();

		return null;
	}

	public static final String SHOW_BOTH_WORK_PLANS_CODE = "";
	public static final String SHOW_ONLY_SHARED_WORK_PLAN_CODE = "ShowOnlySharedWorkPlan";
	public static final String SHOW_ONLY_LEGACY_WORK_PLAN_CODE = "ShowOnlyLegacyWorkPlan";
}
