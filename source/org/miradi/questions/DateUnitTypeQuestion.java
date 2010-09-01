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

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;

public class DateUnitTypeQuestion extends DynamicChoiceQuestion
{
	public DateUnitTypeQuestion(Project projectToUse, ORefList resourceAssignmentRefsToUse)
	{
		project = projectToUse;
		projectCalendar = project.getProjectCalendar();
		resourceAssignmentRefs = resourceAssignmentRefsToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		if (shouldAddNoneChoice())
			choices.add(new ChoiceItem(DateUnitTypeQuestion.NONE_CODE, EAM.text("Unscheduled")));
		
		choices.add(new ChoiceItem(DateUnitTypeQuestion.PROJECT_TOTAL_CODE, EAM.text("Entire Project Duration")));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.YEAR_CODE, EAM.text("Specific Year(s)")));
		if (projectCalendar.shouldShowQuarterColumns())
			choices.add(new ChoiceItem(DateUnitTypeQuestion.QUARTER_CODE, EAM.text("Specific Quarter(s)")));
		
		choices.add(new ChoiceItem(DateUnitTypeQuestion.MONTH_CODE, EAM.text("Specific Month(s)")));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.DAY_CODE, EAM.text("Specific Day(s)")));
		
		return choices.toArray(new ChoiceItem[0]);
	}
	
	private boolean shouldAddNoneChoice()
	{
		try
		{
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

	private Project getProject()
	{
		return project;
	}

	public static final String NONE_CODE = "NoneCode";
	public static final String PROJECT_TOTAL_CODE = "ProjectTotalCode";
	public static final String YEAR_CODE = "YearCode";
	public static final String QUARTER_CODE = "QuarterCode";
	public static final String MONTH_CODE = "MonthCode";
	public static final String DAY_CODE = "DayCode";
	
	private ProjectCalendar projectCalendar;
	private Project project;
	private ORefList resourceAssignmentRefs;
}
