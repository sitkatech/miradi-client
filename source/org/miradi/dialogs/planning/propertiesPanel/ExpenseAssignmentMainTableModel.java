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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.project.Project;
import org.miradi.schemas.ExpenseAssignmentSchema;

public class ExpenseAssignmentMainTableModel extends AbstractAssignmentSummaryTableModel
{
	public ExpenseAssignmentMainTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public String getColumnName(int column)
	{
		if (isExpenseNameColumn(column))
			return EAM.text("Name");
		
		return super.getColumnName(column);
	}
	
	@Override
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		return getCellValue(row, column);
	}
	
	private Object getCellValue(int row, int column)
	{
		if (isExpenseNameColumn(column))
			return getBaseObjectForRowColumn(row, column).getLabel();
		
		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		if (row < 0 || row >= getRowCount())
		{
			EAM.errorDialog(EAM.text("An error has occurred while writing assignment data."));
			EAM.logWarning("Row out of bounds in ExpenseAssignmentMainTableModel.setValueAt value = "+ value + " row = " + row + " column = " + column);
			return;
		}
		
		ORef assignmentRefForRow = getRefForRow(row);
		setExpenseNameCell(value, assignmentRefForRow, column);
		
		super.setValueAt(value, row, column);
	}
	
	private void setExpenseNameCell(Object value, ORef refForRow, int column)
	{
		if (! isExpenseNameColumn(column))
			return;

		String expenseName = value.toString();
		setValueUsingCommand(refForRow, ExpenseAssignment.TAG_LABEL, expenseName);
	}
	
	@Override
	public boolean isResourceColumn(int column)
	{
		return false;
	}
	
	private boolean isExpenseNameColumn(int column)
	{
		return getExpenseNameColumn() == column;
	}

	private int getExpenseNameColumn()
	{
		return EXPENSE_NAME_COLUMN;
	}
	
	@Override
	protected String getListTag()
	{
		return BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS;
	}

	@Override
	protected int getListType()
	{
		return ExpenseAssignmentSchema.getObjectType();
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	private static final String UNIQUE_MODEL_IDENTIFIER = "ExpenseAssignmentMainTableModel";
	
	private static final int COLUMN_COUNT = 1;
	
	private static final int EXPENSE_NAME_COLUMN = 0;
}
