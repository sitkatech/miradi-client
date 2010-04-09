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

package org.miradi.dialogfields.editors;

import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.CalendarYearChoiceQuestion;
import org.miradi.questions.ChoiceQuestion;

public class CalendarYearComboBox extends YearComboBox
{
	public CalendarYearComboBox(ProjectCalendar projectCalendarToUse, DateUnit dateUnit)
	{
		super(projectCalendarToUse, dateUnit);
	}
	
	@Override
	protected ChoiceQuestion createYearQuestion(int startYear, int endYear)
	{
		return new CalendarYearChoiceQuestion(startYear, endYear);
	}
}
