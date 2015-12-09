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

package org.miradi.dialogfields.editors;

import javax.swing.DefaultComboBoxModel;

import org.miradi.dialogs.fieldComponents.PanelComboBox;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import java.util.Arrays;
import java.util.HashSet;

abstract public class DateUnitComboBox extends PanelComboBox
{
	public DateUnitComboBox(ProjectCalendar projectCalendarToUse, DateUnit dateUnit)
	{
		projectCalendar = projectCalendarToUse;
		setComboBoxModel(dateUnit);
		setSelectedDateUnit(dateUnit);
	}

	private void setComboBoxModel(DateUnit dateUnit)
	{
		Object[] choiceItems = getChoiceItems(dateUnit);
		removeAllItems();
		Arrays.sort(choiceItems);
		setModel(new DefaultComboBoxModel(choiceItems));
	}

	private Object[] getChoiceItems(DateUnit dateUnit)
	{
		HashSet<ChoiceItem> choiceItems = new HashSet<ChoiceItem>(Arrays.asList(createChoices()));

		ChoiceItem choiceItem = getChoiceItemForDateUnit(dateUnit);
		if (choiceItem != null)
			choiceItems.add(choiceItem);

		return choiceItems.toArray();
	}

	public DateUnit getDateUnit()
	{
		ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
		if (selectedItem == null)
			return null;
		
		return new DateUnit(selectedItem.getCode());
	}

	protected ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}

	protected void setProjectCalendar(ProjectCalendar projectCalendarToUse)
	{
		projectCalendar = projectCalendarToUse;
	}

	protected void setSelectedDateUnit(DateUnit dateUnit)
	{
		setComboBoxModel(dateUnit);
		ChoiceItem choiceItem = getChoiceItemForDateUnit(dateUnit);
		if (choiceItem != null)
			setSelectedItem(choiceItem);
	}
	
	protected ChoiceItem getChoiceItemForDateUnit(DateUnit dateUnit)
	{
		ChoiceItem choiceItem = null;

		if (dateUnit != null && isType(dateUnit))
		{
			choiceItem = createQuestion().findChoiceByCode(dateUnit.getDateUnitCode());
			if (choiceItem == null)
			{
				String label = getProjectCalendar().getLongDateUnitString(dateUnit);
				choiceItem = new ChoiceItem(dateUnit.getDateUnitCode(), label);
			}
		}

		return choiceItem;
	}

	abstract protected ChoiceItem[] createChoices();
	
	abstract protected ChoiceQuestion createQuestion();
	
	abstract protected boolean isType(DateUnit dateUnit);
	
	private ProjectCalendar projectCalendar;
}
