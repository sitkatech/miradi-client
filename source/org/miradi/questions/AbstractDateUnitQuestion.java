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

import java.util.Vector;

import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;

abstract public class AbstractDateUnitQuestion extends MultiSelectDynamicChoiceQuestion
{
	public AbstractDateUnitQuestion(ProjectCalendar projectCalendarToUse)
	{
		projectCalendar = projectCalendarToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector<DateUnit> dateUnits = getSafeDateUnits();
		Vector<ChoiceItem> choices = new Vector<ChoiceItem>();
		for (DateUnit dateUnit : dateUnits)
		{
			String label = getProjectCalendar().getLongDateUnitString(dateUnit);
			ChoiceItem choiceItem = new ChoiceItem(dateUnit.getDateUnitCode(), label);
			choices.add(choiceItem);
		}
		
		return choices.toArray(new ChoiceItem[0]);
	}
	
	protected ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}
	
	private Vector<DateUnit> getSafeDateUnits()
	{
		try
		{
			return getDateUnits();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	abstract protected Vector<DateUnit> getDateUnits() throws Exception;
	
	private ProjectCalendar projectCalendar;
}
