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

abstract public class AbstractDateUnitQuestion extends DynamicChoiceQuestion
{
	public AbstractDateUnitQuestion(ProjectCalendar projectCalendarToUse)
	{
		projectCalendar = projectCalendarToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<DateUnit> dateUnits = getDateUnits();
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		for (DateUnit dateUnit : dateUnits)
		{
			String label = getProjectCalendar().getShortDateUnitString(dateUnit);
			ChoiceItem choiceItem = new ChoiceItem(dateUnit.getDateUnitCode(), label);
			choices.add(choiceItem);
		}
		
		return choices.toArray(new ChoiceItem[0]);
	}
	
	protected ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}
	
	abstract protected Vector<DateUnit> getDateUnits();
	
	private ProjectCalendar projectCalendar;
}
