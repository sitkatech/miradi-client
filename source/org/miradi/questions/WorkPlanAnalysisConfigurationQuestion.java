/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import java.util.Vector;

public class WorkPlanAnalysisConfigurationQuestion extends WorkPlanColumnConfigurationQuestion
{
	@Override
	protected ChoiceItem[] createChoices()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();

		choiceItems.add(createChoiceItem(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_WORK_UNITS_COLUMN_CODE));
		choiceItems.add(createChoiceItem(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_EXPENSES_CODE));
		choiceItems.add(createChoiceItem(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_BUDGET_DETAILS_COLUMN_CODE));

		return choiceItems.toArray(new ChoiceItem[0]);
	}
}
