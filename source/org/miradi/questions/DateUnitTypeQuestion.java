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
		choices.add(new ChoiceItem(DateUnitTypeQuestion.NONE_ITEM, DateUnitTypeQuestion.NONE_ITEM));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.PROJECT_TOTAL_ITEM, DateUnitTypeQuestion.PROJECT_TOTAL_ITEM));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.YEAR_ITEM, DateUnitTypeQuestion.YEAR_ITEM));
		if (projectCalendar.shouldShowQuarterColumns())
			choices.add(new ChoiceItem(DateUnitTypeQuestion.QUARTER_ITEM, DateUnitTypeQuestion.QUARTER_ITEM));
		
		choices.add(new ChoiceItem(DateUnitTypeQuestion.MONTH_ITEM, DateUnitTypeQuestion.MONTH_ITEM));
		choices.add(new ChoiceItem(DateUnitTypeQuestion.DAY_ITEM, DateUnitTypeQuestion.DAY_ITEM));
		
		return choices.toArray(new ChoiceItem[0]);
	}
	
	public static final String NONE_ITEM = "None";
	public static final String PROJECT_TOTAL_ITEM = "Project Total";
	public static final String YEAR_ITEM = "Year";
	public static final String QUARTER_ITEM = "Quarter";
	public static final String MONTH_ITEM = "Month";
	public static final String DAY_ITEM = "Day";
	
	private ProjectCalendar projectCalendar;
}
