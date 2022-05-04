/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.ResourceAssignmentSchema;

import java.util.Vector;

public class WorkPlanRowConfigurationQuestion extends MultipleSelectStaticChoiceQuestion
{
	public WorkPlanRowConfigurationQuestion()
	{
		super();
	}

	@Override
	protected ChoiceItem[] createChoices()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(new ChoiceItem(RESOURCE_ASSIGNMENT, EAM.text("Assignments")));
		choiceItems.add(new ChoiceItem(EXPENSE_ASSIGNMENT, EAM.text("Projected Expenses")));

		return choiceItems.toArray(new ChoiceItem[0]);
	}

	public static final String RESOURCE_ASSIGNMENT = ResourceAssignmentSchema.OBJECT_NAME;
	public static final String EXPENSE_ASSIGNMENT = ExpenseAssignmentSchema.OBJECT_NAME;
}
