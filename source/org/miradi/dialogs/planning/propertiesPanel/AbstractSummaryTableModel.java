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

import org.miradi.dialogs.planning.treenodes.UnspecifiedBaseObject;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.ColumnTagProvider;

abstract public class AbstractSummaryTableModel extends PlanningViewAbstractAssignmentTableModel implements ColumnTagProvider
{
	public AbstractSummaryTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return true;
	}
	
	@Override
	public String getColumnName(int column)
	{
		if (isAccountingCodeColumn(column))
			return EAM.text("Acct Code");
		
		if (isFundingSourceColumn(column))
			return EAM.text("Funding Source");
		
		if (isBudgetCategoryOneColumn(column))
			return EAM.text("Category One");
		
		if (isBudgetCategoryTwoColumn(column))
			return EAM.text("Category Two");
		
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
		
		if (isBudgetCategoryOneColumn(column))
			return getBaseObject(baseObjectForRow, Assignment.TAG_CATEGORY_ONE_REF, BudgetCategoryOne.getObjectType(), BudgetCategoryOne.OBJECT_NAME);
		
		if (isBudgetCategoryTwoColumn(column))
			return getBaseObject(baseObjectForRow, Assignment.TAG_CATEGORY_TWO_REF, BudgetCategoryTwo.getObjectType(), BudgetCategoryTwo.OBJECT_NAME);
		
		return null;
	}
	
	private BaseObject getBaseObject(BaseObject baseObjectForRow, String tagForRef, int objectType, String objectTypeName)
	{
		ORef ref = baseObjectForRow.getRef(tagForRef);
		if (ref.isInvalid())
			return createInvalidObject(getObjectManager(), objectType, objectTypeName);
			
		return BaseObject.find(getProject(), ref);
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		ORef refForRow = getRefForRow(row);
		if (isAccountingCodeColumn(column))
			setAccountingCode((BaseObject)value, refForRow, column);
		
		if (isFundingSourceColumn(column))
			setFundingSource((BaseObject) value, refForRow, column);
		
		if (isBudgetCategoryOneColumn(column))
			setRefValue((BaseObject) value, Assignment.TAG_CATEGORY_ONE_REF, refForRow);
		
		if (isBudgetCategoryTwoColumn(column))
			setRefValue((BaseObject) value, Assignment.TAG_CATEGORY_TWO_REF, refForRow);
	}
	
	private void setRefValue(BaseObject baseObject, String destinationTag, ORef refForRow)
	{
		setValueUsingCommand(refForRow, destinationTag, baseObject.getRef());
	}

	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
	
	public static BaseObject createInvalidObject(ObjectManager objectManager, int objectType, String objectTypeName)
	{
		return new UnspecifiedBaseObject(objectManager, objectType, objectTypeName);
	}
	
	public boolean isColumnForType(int column, int objectType)
	{
		if (FundingSource.is(objectType))
			return isFundingSourceColumn(column);
		
		if (AccountingCode.is(objectType))
			return isAccountingCodeColumn(column);
		
		if (BudgetCategoryOne.is(objectType))
			return isBudgetCategoryOneColumn(column);
		
		if (BudgetCategoryTwo.is(objectType))
			return isBudgetCategoryTwoColumn(column);
		
		return false;
	}
	
	abstract protected BaseObject getFundingSource(BaseObject assignment);
	
	abstract protected BaseObject getAccountingCode(BaseObject assignment);

	abstract public boolean isFundingSourceColumn(int column);

	abstract public boolean isAccountingCodeColumn(int column);
	
	abstract public boolean isBudgetCategoryOneColumn(int column);
	
	abstract public boolean isBudgetCategoryTwoColumn(int column);

	abstract public int getColumnCount();
	
	abstract public boolean isResourceColumn(int column);
	
	abstract protected String getAccountingCodeTag();
	
	abstract protected String getFundingSourceTag();
	
	abstract protected void setAccountingCode(BaseObject value, ORef assignmentRefForRow, int column);

	abstract protected void setFundingSource(BaseObject value, ORef assignmentRefForRow, int column);
}
