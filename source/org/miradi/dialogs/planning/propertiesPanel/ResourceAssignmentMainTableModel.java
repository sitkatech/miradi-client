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
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;

public class ResourceAssignmentMainTableModel extends AbstractSummaryTableModel
{
	public ResourceAssignmentMainTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		if (isResourceCostPerUnitColumn(column))
			return false;
		
		return super.isCellEditable(row, column);
	}
	
	@Override
	public String getColumnName(int column)
	{
		if (isResourceColumn(column))
			return EAM.text("Resource (Who)");
		
		if (isResourceCostPerUnitColumn(column))
			return EAM.text("Daily Rate");
			
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
	
	@Override
	protected Object getCellValue(int row, int column)
	{
		ORef resourceAssignmentRef = getRefForRow(row);
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRef);
		if (isResourceColumn(column))
			return findProjectResource(resourceAssignment);
		
		if (isResourceCostPerUnitColumn(column))
			return getResourceCostPerUnit(resourceAssignment);	
		
		return super.getCellValue(row, column);
	}
	
	//FIXME medium: planning table - there should be methods that return the raw value,  then that value
	//can be used in budgetmodel to calculate the cost. (cost per unit and units need to return raw values)
	private Object getResourceCostPerUnit(ResourceAssignment resourceAssignment)
	{
		ProjectResource resource = findProjectResource(resourceAssignment);
		if (resource == null)
			return "";
				
		double cost = resource.getCostPerUnit();
		return currencyFormatter.format(cost);
	}
	
	@Override
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
		
		ORef resourceAssignmentRefForRow = getRefForRow(row);
		setResourceCell(value, resourceAssignmentRefForRow, column);
		
		super.setValueAt(value, row, column);
	}
	
	private void setResourceCell(Object value, ORef resourceAssignmentRefForRow, int column)
	{
		if (! isResourceColumn(column))
			return;

		ProjectResource projectResource = (ProjectResource)value;
		BaseId resourceId = projectResource.getId();
		setValueUsingCommand(resourceAssignmentRefForRow, ResourceAssignment.TAG_RESOURCE_ID, resourceId);
	}
	
	private ProjectResource findProjectResource(ResourceAssignment resourceAssignment)
	{
		ORef resourceRef = resourceAssignment.getResourceRef();
		if (resourceRef.isInvalid())
			return createInvalidResource(getObjectManager());
		
		return ProjectResource.find(getProject(), resourceRef);
	}
		
	@Override
	public boolean isResourceColumn(int column)
	{
		return getResourceColumn() == column;
	}

	@Override
	public boolean isFundingSourceColumn(int column)
	{
		return getFundingSourceColumn() == column;
	}

	@Override
	public boolean isAccountingCodeColumn(int column)
	{
		return getAccountingCodeColumn() == column;
	}

	public boolean isResourceCostPerUnitColumn(int column)
	{
		return getResourceCostPerUnitColumn() == column;
	}

	protected int getResourceCostPerUnitColumn()
	{
		return RESOURCE_COST_PER_UNIT_COLUMN;
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
	
	@Override
	protected String getListTag()
	{
		return BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;
	}

	@Override
	protected int getListType()
	{
		return ResourceAssignment.getObjectType();
	}
	
	@Override
	protected BaseObject getFundingSource(BaseObject baseObjectToUse)
	{
		ResourceAssignment resourceAssignment = castToAssignment(baseObjectToUse);
		ORef fundingSourceRef = resourceAssignment.getFundingSourceRef();
		if (fundingSourceRef.isInvalid())
			return createInvalidFundingSource(getObjectManager());
			
		return FundingSource.find(getProject(), fundingSourceRef);
	}

	@Override
	protected BaseObject getAccountingCode(BaseObject baseObjectToUse)
	{
		ResourceAssignment assignment = castToAssignment(baseObjectToUse);
		ORef accountingCodeRef = assignment.getAccountingCodeRef();
		if (accountingCodeRef.isInvalid())
			return createInvalidAccountingCode(getObjectManager());
		
		return AccountingCode.find(getProject(), accountingCodeRef);
	}
	
	private ResourceAssignment castToAssignment(BaseObject baseObjectToUse)
	{
		return (ResourceAssignment) baseObjectToUse;
	}
		
	@Override
	protected String getAccountingCodeTag()
	{
		return ResourceAssignment.TAG_ACCOUNTING_CODE;
	}
	
	@Override
	protected String getFundingSourceTag()
	{
		return ResourceAssignment.TAG_FUNDING_SOURCE;
	}

	@Override
	protected void setAccountingCode(Object value, ORef assignmentRefForRow, int column)
	{
		AccountingCode accountingCode = (AccountingCode)value;
		BaseId accountingCodeId = accountingCode.getId();
		setValueUsingCommand(assignmentRefForRow, getAccountingCodeTag(), accountingCodeId);
	}

	@Override
	protected void setFundingSource(Object value, ORef assignmentRefForRow, int column)
	{
		FundingSource fundingSource = (FundingSource)value;
		BaseId fundingSourceId = fundingSource.getId();
		setValueUsingCommand(assignmentRefForRow, getFundingSourceTag(), fundingSourceId);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	public static ProjectResource createInvalidResource(ObjectManager objectManager)
	{
		try
		{
			ProjectResource invalidResource = new ProjectResource(objectManager, BaseId.INVALID);
			invalidResource.setData(ProjectResource.TAG_GIVEN_NAME, "(" + EAM.text("Not Specified") + ")");
	
			return invalidResource;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}
	
	public static FundingSource createInvalidFundingSource(ObjectManager objectManager)
	{
		return new FundingSource(objectManager, BaseId.INVALID);
	}
	
	public static AccountingCode createInvalidAccountingCode(ObjectManager objectManager)
	{
		return new AccountingCode(objectManager, BaseId.INVALID);
	}

	private static final String UNIQUE_MODEL_IDENTIFIER = "ResourceAssignmentMainTableModel";
	
	private static final int COLUMN_COUNT = 4;
	
	private static final int RESOURCE_COLUMM = 0;
	private static final int RESOURCE_COST_PER_UNIT_COLUMN = 1;
	private static final int ACCOUNTING_CODE_COLUMN = 2;
	private static final int FUNDING_SOURCE_COLUMN = 3;
}
