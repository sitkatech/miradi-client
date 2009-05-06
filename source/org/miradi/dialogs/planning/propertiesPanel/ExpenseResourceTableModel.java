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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class ExpenseResourceTableModel extends PlanningViewResourceTableModel
{
	public ExpenseResourceTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public String getColumnName(int column)
	{
		if (isExpenseNameColumn(column))
			return EAM.text("Name");
		
		return super.getColumnName(column);
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public Object getValueAt(int row, int column)
	{
		return getCellValue(row, column);
	}
	
	protected Object getCellValue(int row, int column)
	{
		if (isExpenseNameColumn(column))
			return "DUMMY EXPENSE NAME";
		
		return super.getCellValue(row, column);
	}

	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		if (row < 0 || row >= getRowCount())
		{
			EAM.errorDialog(EAM.text("An error has occured while writing assignment data."));
			EAM.logWarning("Row out of bounds in PlanningViewResourceTableModel.setValueAt value = "+ value + " row = " + row + " column = " + column);
			return;
		}
		
		ORef assignmentRefForRow = getAssignmentForRow(row);
		setExpenseNameCell(value, assignmentRefForRow, column);
		
		super.setValueAt(value, row, column);
	}
	
	private void setExpenseNameCell(Object value, ORef assignmentRefForRow, int column)
	{
		if (! isExpenseNameColumn(column))
			return;

		ProjectResource projectResource = (ProjectResource)value;
		BaseId resourceId = projectResource.getId();
		setValueUsingCommand(assignmentRefForRow, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId);
	}
	
	public boolean isResourceColumn(int column)
	{
		return false;
	}
	
	public boolean isExpenseNameColumn(int column)
	{
		return getExpenseNameColumn() == column;
	}

	public boolean isFundingSourceColumn(int column)
	{
		return getFundingSourceColumn() == column;
	}

	public boolean isAccountingCodeColumn(int column)
	{
		return getAccountingCodeColumn() == column;
	}

	private int getAccountingCodeColumn()
	{
		return ACCOUNTING_CODE_COLUMN;
	}
	
	private int getFundingSourceColumn()
	{
		return FUNDING_SOURCE_COLUMN;
	}
	
	private int getExpenseNameColumn()
	{
		return EXPENSE_NAME_COLUMN;
	}
						
	private static final int COLUMN_COUNT = 3;
	
	private static final int EXPENSE_NAME_COLUMN = 0;
	private static final int ACCOUNTING_CODE_COLUMN = 1;
	private static final int FUNDING_SOURCE_COLUMN = 2;
}
