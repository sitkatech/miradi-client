/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

public class WorkPlanColumnConfigurationQuestion extends StaticChoiceQuestion
{
	public WorkPlanColumnConfigurationQuestion()
	{
		super(getColumnChoiceItems());
	}

	private static ChoiceItem[] getColumnChoiceItems()
	{
		return new ChoiceItem[] 
		{
				createChoiceItem(CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE),
				createChoiceItem(CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE),
				createChoiceItem(CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE),
		};
	}
	
	private static ChoiceItem createChoiceItem(String tag)
	{
		return CustomPlanningColumnsQuestion.createChoiceItem(tag);
	}

	public static String getNormalizedBudgetGroupColumnCode(String budgetColumnGroupCode)
	{
		if (getAllPossibleWorkUnitsColumnGroups().contains(budgetColumnGroupCode))
			return CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE;
		
		if (getAllPossibleExpensesColumnGroups().contains(budgetColumnGroupCode))
			return CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE;
		
		if (getAllPossibleBudgetTotalsColumnGroups().contains(budgetColumnGroupCode))
			return CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE;
		
		throw new RuntimeException("Column code is not a budet column. Code: " + budgetColumnGroupCode);
	}
	
	public static Vector<String> getAllPossibleWorkUnitsColumnGroups()
	{
		Vector<String> columnGroups = new Vector();
		columnGroups.add(CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE);
		columnGroups.add(CustomPlanningColumnsQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE);
		
		return columnGroups;
	}

	public static Vector<String> getAllPossibleExpensesColumnGroups()
	{
		Vector<String> columnGroups = new Vector();
		columnGroups.add(CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE);
		columnGroups.add(CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE);
		columnGroups.add(CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE);
		
		return columnGroups;
	}

	public static Vector<String> getAllPossibleBudgetTotalsColumnGroups()
	{
		Vector<String> columnGroups = new Vector();
		columnGroups.add(CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE);
		columnGroups.add(CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE);
		columnGroups.add(CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE);
		
		return columnGroups;
	}
}
