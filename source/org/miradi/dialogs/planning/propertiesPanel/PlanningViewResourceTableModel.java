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
import org.miradi.objects.FundingSource;
import org.miradi.project.Project;
import org.miradi.utils.ColumnTagProvider;

abstract public class PlanningViewResourceTableModel extends PlanningViewAbstractAssignmentTableModel implements ColumnTagProvider
{
	public PlanningViewResourceTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean isCellEditable(int row, int column)
	{
		return true;
	}
	
	public String getColumnName(int column)
	{
		if (isAccountingCodeColumn(column))
			return EAM.text("Acct Code");
		
		if (isFundingSourceColumn(column))
			return EAM.text("Funding Source");
		
		return null;
	}
	
	public Object getValueAt(int row, int column)
	{
		return getCellValue(row, column);
	}
	
	protected Object getCellValue(int row, int column)
	{
		ORef assignmentRef = getRefForRow(row);
		Assignment assignment = (Assignment) getProject().findObject(assignmentRef);
		if (isFundingSourceColumn(column))
			return getFundingSource(assignment);
		
		if (isAccountingCodeColumn(column))
			return getAccountingCode(assignment);
		
		return null;
	}

	public void setValueAt(Object value, int row, int column)
	{
		ORef assignmentRefForRow = getRefForRow(row);
		setAccountingCode(value, assignmentRefForRow, column);
		setFundingSource(value, assignmentRefForRow, column);
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

	private BaseObject findObject(ORef ref)
	{
		return getProject().findObject(ref);
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}

	abstract public boolean isFundingSourceColumn(int column);

	abstract public boolean isAccountingCodeColumn(int column);

	abstract public int getColumnCount();
	
	abstract public boolean isResourceColumn(int column);
}
