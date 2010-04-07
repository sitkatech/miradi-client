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

import org.miradi.dialogs.fieldComponents.PanelComboBox;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.CalendarYearChoiceQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.FiscalYearChoiceQuestion;

public class YearComboBox extends PanelComboBox
{
	public YearComboBox(ProjectCalendar projectCalendar)
	{
		super(createChoices(projectCalendar));
	}
	
	public int getYear()
	{
		ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
		return Integer.parseInt(selectedItem.getCode());
	}

	private static ChoiceItem[] createChoices(ProjectCalendar projectCalendar)
	{
		return createYearQuestion(projectCalendar).getChoices();
	}
	
	private static ChoiceQuestion createYearQuestion(ProjectCalendar projectCalendar)
	{
		int startYear = projectCalendar.getPlanningStartMultiCalendar().getGregorianYear();
		int endYear = projectCalendar.getPlanningEndMultiCalendar().getGregorianYear();
		int fiscalYearStartMonth = projectCalendar.getFiscalYearFirstMonth();
		if (fiscalYearStartMonth == 1)
			return new CalendarYearChoiceQuestion(startYear, endYear);	
		
		return new FiscalYearChoiceQuestion(startYear, endYear);
	}
}
