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
		
		if (isCategoryOneColumn(column))
			return EAM.text("Category One");
		
		if (isCategoryTwoColumn(column))
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
		
		if (isCategoryOneColumn(column))
			return getBaseObject(baseObjectForRow, Assignment.TAG_CATEGORY_ONE_REF, BudgetCategoryOne.getObjectType());
		
		if (isCategoryTwoColumn(column))
			return getBaseObject(baseObjectForRow, Assignment.TAG_CATEGORY_TWO_REF, BudgetCategoryTwo.getObjectType());
		
		return null;
	}
	
	protected BaseObject getBaseObject(BaseObject baseObjectForRow, String tagForRef, int objectType)
	{
		ORef ref = baseObjectForRow.getRef(tagForRef);
		if (ref.isInvalid())
			return createInvalidObject(getObjectManager(), objectType);
			
		return BaseObject.find(getProject(), ref);
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		ORef refForRow = getRefForRow(row);
		if (isAccountingCodeColumn(column))
			setAccountingCode(value, refForRow, column);
		
		if (isFundingSourceColumn(column))
			setFundingSource(value, refForRow, column);
		
		if (isCategoryOneColumn(column))
			setRefValue((BaseObject) value, Assignment.TAG_CATEGORY_ONE_REF, refForRow);
		
		if (isCategoryTwoColumn(column))
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
	
	//FIXME medium - this method should not create invalid objects. Having invalid objects, might be saved to disk.  
	//Should use some unspecified object,  see how unspecified nodes are created in Funding source tree. 
	public static BaseObject createInvalidObject(ObjectManager objectManager, int objectType)
	{
		if (AccountingCode.is(objectType))
			return new AccountingCode(objectManager, BaseId.INVALID);
		
		if (FundingSource.is(objectType))
			return new FundingSource(objectManager, BaseId.INVALID);
		
		if (BudgetCategoryOne.is(objectType))
			return new BudgetCategoryOne(objectManager, BaseId.INVALID);
		
		if (BudgetCategoryTwo.is(objectType))
			return new BudgetCategoryTwo(objectManager, BaseId.INVALID);
		
		throw new RuntimeException("createInvalidObject does not handle the object type: " + objectType);
	}
	
	public boolean isColumnForType(int column, int objectType)
	{
		if (FundingSource.is(objectType))
			return isFundingSourceColumn(column);
		
		if (AccountingCode.is(objectType))
			return isAccountingCodeColumn(column);
		
		if (BudgetCategoryOne.is(objectType))
			return isCategoryOneColumn(column);
		
		if (BudgetCategoryTwo.is(objectType))
			return isCategoryTwoColumn(column);
		
		return false;
	}
	
	abstract protected BaseObject getFundingSource(BaseObject assignment);
	
	abstract protected BaseObject getAccountingCode(BaseObject assignment);

	abstract public boolean isFundingSourceColumn(int column);

	abstract public boolean isAccountingCodeColumn(int column);
	
	abstract public boolean isCategoryOneColumn(int column);
	
	abstract public boolean isCategoryTwoColumn(int column);

	abstract public int getColumnCount();
	
	abstract public boolean isResourceColumn(int column);
	
	abstract protected String getAccountingCodeTag();
	
	abstract protected String getFundingSourceTag();
	
	abstract protected void setAccountingCode(Object value, ORef assignmentRefForRow, int column);

	abstract protected void setFundingSource(Object value, ORef assignmentRefForRow, int column);
}
