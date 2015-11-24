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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;

import java.util.Vector;

public class AssignedDateUnitTypeQuestion extends AbstractDateUnitTypeQuestion
{
	public AssignedDateUnitTypeQuestion(Project projectToUse, ORefList resourceAssignmentRefsToUse)
	{
		super(projectToUse, resourceAssignmentRefsToUse);
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();

		if (shouldAddNoneChoice())
			choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.NONE_CODE, EAM.text("Unscheduled")));
		choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.PROJECT_TOTAL_CODE, EAM.text("Entire Project Duration")));
		choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.YEAR_CODE, EAM.text("Specific Year(s)")));
		if (getProjectCalendar().shouldShowQuarterColumns())
			choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.QUARTER_CODE, EAM.text("Specific Quarter(s)")));
		choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.MONTH_CODE, EAM.text("Specific Month(s)")));
		choices.add(new ChoiceItem(AbstractDateUnitTypeQuestion.DAY_CODE, EAM.text("Specific Day(s)")));

		return choices.toArray(new ChoiceItem[0]);
	}

	private boolean shouldAddNoneChoice()
	{
		try
		{
			ORefList resourceAssignmentRefs = getDateUnitOwnerRefs();
			for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
			{
				ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRefs.get(index));
				if (resourceAssignment.hasCategoryData())
					return false;
			}

			return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
}
