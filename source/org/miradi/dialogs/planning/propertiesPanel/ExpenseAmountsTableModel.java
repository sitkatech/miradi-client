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

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ColumnConfigurationQuestion;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.OptionalDouble;

public class ExpenseAmountsTableModel extends AssignmentDateUnitsTableModel
{
	public ExpenseAmountsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);
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
		return ColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE;
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateRange dateRange) throws Exception
	{
		return baseObject.getExpenseAmounts(dateRange);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	@Override
	protected boolean isAssignmentCellEditable(int row, int column) throws Exception
	{
		DateRange dateRange = getDateRange(column);
		Assignment assignment = getAssignment(row);
		if (!ExpenseAssignment.is(assignment))
			return false;
		
		DateRangeEffort thisCellEffort = getDateRangeEffort(assignment, dateRange);
		if(thisCellEffort != null)
			return true;
		
		return !assignment.getExpenseAmounts(dateRange).hasValue();
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "ExpenseAmountsTableModel";
}
