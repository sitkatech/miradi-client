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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
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
		ORef baseObjectRefForRow = getRefForRow(row);
		BaseObject baseObjectForRow = getProject().findObject(baseObjectRefForRow);
		if (isFundingSourceColumn(column))
			return getFundingSource(baseObjectForRow);
		
		if (isAccountingCodeColumn(column))
			return getAccountingCode(baseObjectForRow);
		
		return null;
	}

	public void setValueAt(Object value, int row, int column)
	{
		ORef refForRow = getRefForRow(row);
		if (isAccountingCodeColumn(column))
			setAccountingCode(value, refForRow, column);
		
		if (isFundingSourceColumn(column))
			setFundingSource(value, refForRow, column);
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	abstract protected BaseObject getFundingSource(BaseObject assignment);
	
	abstract protected BaseObject getAccountingCode(BaseObject assignment);

	abstract public boolean isFundingSourceColumn(int column);

	abstract public boolean isAccountingCodeColumn(int column);

	abstract public int getColumnCount();
	
	abstract public boolean isResourceColumn(int column);
	
	abstract protected String getAccountingCodeTag();
	
	abstract protected String getFundingSourceTag();
	
	abstract protected void setAccountingCode(Object value, ORef assignmentRefForRow, int column);

	abstract protected void setFundingSource(Object value, ORef assignmentRefForRow, int column);
}
