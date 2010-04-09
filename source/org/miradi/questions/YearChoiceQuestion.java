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

import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;

public class YearChoiceQuestion extends AbstractDateUnitQuestion
{
	public YearChoiceQuestion(ProjectCalendar projectCalendarToUse)
	{
		projectCalendar = projectCalendarToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<DateUnit> allProjectYearDateUnits = getAllProjectYearDateUnits();
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		for (DateUnit yearDateUnit : allProjectYearDateUnits)
		{
			String yearLabel = getProjectCalendar().getShortDateUnitString(yearDateUnit);
			ChoiceItem yearChoiceItem = new ChoiceItem(yearDateUnit.getDateUnitCode(), yearLabel);
			choices.add(yearChoiceItem);
		}
		
		return choices.toArray(new ChoiceItem[0]);
	}

	private Vector<DateUnit> getAllProjectYearDateUnits()
	{
		try
		{
			return getProjectCalendar().getAllProjectYearDateUnits();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}
	
	private ProjectCalendar projectCalendar;
}
