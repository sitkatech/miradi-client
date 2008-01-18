/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

public class PlanningViewResourceTableModel extends PlanningViewAbstractAssignmentTabelModel implements ColumnTagProvider
{
	public PlanningViewResourceTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		if (isResourceCostPerUnitColumn(column))
			return false;
		
		if (isResourceCostColumn(column))
			return false;
		
		return true;
	}
	
	public String getColumnName(int column)
	{
		if (isResourceColumn(column))
			return EAM.text("Resource (Who)");
		
		if (isResourceCostPerUnitColumn(column))
			return EAM.text("Cost/Unit");
			
		if (isResourceCostColumn(column))
			return EAM.text("Unit");
		
		if (isAccountingCodeColumn(column))
			return EAM.text("Acct Code");
		
		if (isFundingSourceColumn(column))
			return EAM.text("Funding Source");
		
		return null;
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public Object getValueAt(int row, int column)
	{
		return getCellValue(row, column);
	}
	
	private Object getCellValue(int row, int column)
	{
		ORef assignmentRef = getAssignmentForRow(row);
		Assignment assignment = (Assignment) getProject().findObject(assignmentRef);
		if (isResourceColumn(column))
			return getResource(assignment);
		
		if (isResourceCostColumn(column))
			return getResourceCost(assignment);
		
		if (isResourceCostPerUnitColumn(column))
			return getResourceCostPerUnit(assignment);
		
		if (isFundingSourceColumn(column))
			return getFundingSource(assignment);
		
		if (isAccountingCodeColumn(column))
			return getAccountingCode(assignment);
		
		
		return null;
	}

	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		ORef assignmentRefForRow = getAssignmentForRow(row);
		setResourceCell(value, assignmentRefForRow, column);
		setAccountingCode(value, assignmentRefForRow, column);
		setFundingSource(value, assignmentRefForRow, column);
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
	
	private void setFundingSource(Object value, ORef assignmentRefForRow, int column)
	{
		if (! isFundingSourceColumn(column))
			return;
		
		FundingSource fundingSource = (FundingSource)value;
		BaseId fundingSourceId = fundingSource.getId();
		setValueUsingCommand(assignmentRefForRow, Assignment.TAG_FUNDING_SOURCE, fundingSourceId);
	}

	private BaseObject getResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		return findObject(resourceRef);
	}
	
	private BaseObject getFundingSource(Assignment assignment)
	{
		ORef fundingSourceRef = assignment.getFundingSourceRef();
		return findObject(fundingSourceRef);
	}
	
	private BaseObject getAccountingCode(Assignment assignment)
	{
		ORef accountingCodeRef = assignment.getAccountingCodeRef();
		return findObject(accountingCodeRef);
	}
	
	private String getResourceCost(Assignment assignment)
	{
		ProjectResource resource = findProjectResource(assignment);
		if (resource == null)
			return "";
		
		return resource.getCostUnit();
	}
	
	private BaseObject findObject(ORef ref)
	{
		return getProject().findObject(ref);
	}

	public boolean isResourceColumn(int column)
	{
		return RESOURCE_COLUMM == column;
	}
	
	public boolean isFundingSourceColumn(int column)
	{
		return FUNDING_SOURCE_COLUMN == column;
	}
	
	public boolean isAccountingCodeColumn(int column)
	{
		return ACCOUNTING_CODE_COLUMN == column;
	}
	
	public boolean isResourceCostColumn(int column)
	{
		return RESOURCE_COST_COLUMN == column;
	}
	
	public boolean isResourceCostPerUnitColumn(int column)
	{
		return RESOURCE_COST_PER_UNIT_COLUMN == column;
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	private static final int COLUMN_COUNT = 5;
	
	private static final int RESOURCE_COLUMM = 0;
	private static final int RESOURCE_COST_COLUMN = 1;
	private static final int RESOURCE_COST_PER_UNIT_COLUMN = 2;
	private static final int ACCOUNTING_CODE_COLUMN = 3;
	private static final int FUNDING_SOURCE_COLUMN = 4;
}
