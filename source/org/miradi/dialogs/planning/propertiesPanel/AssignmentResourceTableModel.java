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
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class AssignmentResourceTableModel extends PlanningViewResourceTableModel
{
	public AssignmentResourceTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		if (isResourceCostPerUnitColumn(column))
			return false;
		
		if (isResourceCostColumn(column))
			return false;
		
		return super.isCellEditable(row, column);
	}
	
	public String getColumnName(int column)
	{
		if (isResourceColumn(column))
			return EAM.text("Resource (Who)");
		
		if (isResourceCostPerUnitColumn(column))
			return EAM.text("Cost/Unit");
			
		if (isResourceCostColumn(column))
			return EAM.text("Unit");
		
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
		ORef assignmentRef = getAssignmentForRow(row);
		Assignment assignment = (Assignment) getProject().findObject(assignmentRef);
		if (isResourceColumn(column))
			return getResource(assignment);
		
		if (isResourceCostColumn(column))
			return getResourceCost(assignment);
		
		if (isResourceCostPerUnitColumn(column))
			return getResourceCostPerUnit(assignment);	
		
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
		setResourceCell(value, assignmentRefForRow, column);
		
		super.setValueAt(value, row, column);
	}
	
	private void setResourceCell(Object value, ORef assignmentRefForRow, int column)
	{
		if (! isResourceColumn(column))
			return;

		ProjectResource projectResource = (ProjectResource)value;
		BaseId resourceId = projectResource.getId();
		setValueUsingCommand(assignmentRefForRow, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId);
	}
	
	public void setAccountingCode(Object value, ORef assignmentRefForRow, int column)
	{
		if (! isAccountingCodeColumn(column))
			return;
		
		AccountingCode accountingCode = (AccountingCode)value;
		BaseId accountingCodeId = accountingCode.getId();
		setValueUsingCommand(assignmentRefForRow, Assignment.TAG_ACCOUNTING_CODE, accountingCodeId);
	}
	
	private BaseObject getResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		return findObject(resourceRef);
	}
	
	private String getResourceCost(Assignment assignment)
	{
		ProjectResource resource = findProjectResource(assignment);
		if (resource == null)
			return "";
		
		return resource.getCostUnitValue();
	}
	
	private BaseObject findObject(ORef ref)
	{
		return getProject().findObject(ref);
	}

	public boolean isResourceColumn(int column)
	{
		return getResourceColumn() == column;
	}

	public boolean isFundingSourceColumn(int column)
	{
		return getFundingSourceColumn() == column;
	}

	public boolean isAccountingCodeColumn(int column)
	{
		return getAccountingCodeColumn() == column;
	}

	public boolean isResourceCostColumn(int column)
	{
		return getResourceCostColumn() == column;
	}

	public boolean isResourceCostPerUnitColumn(int column)
	{
		return getResourceCostPerUnitColumn() == column;
	}

	protected int getResourceCostPerUnitColumn()
	{
		return RESOURCE_COST_PER_UNIT_COLUMN;
	}
	
	protected int getResourceCostColumn()
	{
		return RESOURCE_COST_COLUMN;
	}
	
	private int getAccountingCodeColumn()
	{
		return ACCOUNTING_CODE_COLUMN;
	}
	
	private int getFundingSourceColumn()
	{
		return FUNDING_SOURCE_COLUMN;
	}
	
	private int getResourceColumn()
	{
		return RESOURCE_COLUMM;
	}
			
	private static final int COLUMN_COUNT = 5;
	
	private static final int RESOURCE_COLUMM = 0;
	private static final int RESOURCE_COST_COLUMN = 1;
	private static final int RESOURCE_COST_PER_UNIT_COLUMN = 2;
	private static final int ACCOUNTING_CODE_COLUMN = 3;
	private static final int FUNDING_SOURCE_COLUMN = 4;
}
