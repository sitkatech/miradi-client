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

package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;

import org.miradi.dialogs.planning.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.project.Project;
import org.miradi.questions.CurrencyFormattedChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.OptionalDouble;

abstract public class AbstractExpenseTableModel extends AssignmentDateUnitsTableModel
{
	public AbstractExpenseTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	protected OptionalDouble calculateValue(TimePeriodCosts timePeriodCosts) throws Exception
	{
		return timePeriodCosts.getTotalExpense();
	}
	
	@Override
	protected boolean isAssignmentForModel(Assignment assignment)
	{
		return ExpenseAssignment.is(assignment);
	}
	
	@Override
	public boolean isCurrencyColumn(int column)
	{
		return true;
	}
	
	@Override
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getExpenseAmountBackgroundColor(dateUnit);
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE;
	}
		
	@Override
	protected String getAssignmentsTag()
	{
		 return BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS;
	}
	
	@Override
	protected int getAssignmentType()
	{
		return ExpenseAssignment.getObjectType();
	}

	@Override
	protected TaglessChoiceItem createFormattedChoiceItem(OptionalDouble optionalDouble)
	{
		return new CurrencyFormattedChoiceItem(getCurrencyFormatter(), optionalDouble.getValue());
	}
}
