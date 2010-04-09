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

import org.miradi.project.ProjectCalendar;

public class DateUnitTypeQuestion extends DynamicChoiceQuestion
{
	public DateUnitTypeQuestion(ProjectCalendar projectCalendarToUse)
	{
		projectCalendar = projectCalendarToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		choices.add(new ChoiceItem(DateUnitTypeQuestion.NONE_CODE, DateUnitTypeQuestion.NONE_CODE));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.PROJECT_TOTAL_CODE, DateUnitTypeQuestion.PROJECT_TOTAL_CODE));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.YEAR_CODE, DateUnitTypeQuestion.YEAR_CODE));
		if (projectCalendar.shouldShowQuarterColumns())
			choices.add(new ChoiceItem(DateUnitTypeQuestion.QUARTER_CODE, DateUnitTypeQuestion.QUARTER_CODE));
		
		choices.add(new ChoiceItem(DateUnitTypeQuestion.MONTH_CODE, DateUnitTypeQuestion.MONTH_CODE));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.DAY_CODE, DateUnitTypeQuestion.DAY_CODE));
		
		return choices.toArray(new ChoiceItem[0]);
	}
	
	public static final String NONE_CODE = "None";
	public static final String PROJECT_TOTAL_CODE = "Project Total";
	public static final String YEAR_CODE = "Year";
	public static final String QUARTER_CODE = "Quarter";
	public static final String MONTH_CODE = "Month";
	public static final String DAY_CODE = "Day";
	
	private ProjectCalendar projectCalendar;
}
