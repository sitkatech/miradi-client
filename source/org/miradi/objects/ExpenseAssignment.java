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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.ORefData;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;

public class ExpenseAssignment extends Assignment
{
	public ExpenseAssignment(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public ExpenseAssignment(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	@Override
	public OptionalDouble getExpenseAmounts(DateUnit dateUnitToUse) throws Exception
	{
		DateRange dateRange = getProject().getProjectCalendar().convertToDateRange(dateUnitToUse);
		return getTimePeriodCostsMap(dateRange).getTotalCost(dateUnitToUse).getExpense();
	}
	
	protected TimePeriodCostsMap getTimePeriodCostsMap(DateRange dateRangeToUse) throws Exception
	{
		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();
		DateUnitEffortList duel = getDateUnitEffortList();
		for (int index = 0; index < duel.size(); ++index)
		{
			DateUnitEffort due = duel.getDateUnitEffort(index);
			OptionalDouble expense = new OptionalDouble(due.getQuantity());
			tpcm.add(due.getDateUnit(), new TimePeriodCosts(expense));
		}
		
		return tpcm;
	}
	
	public ORef getFundingSourceRef()
	{
		if (fundingSourceRef.getRawRef().isInvalid())
			return ORef.createInvalidWithType(FundingSource.getObjectType());
		
		return fundingSourceRef.getRawRef();
	}
	
	public ORef getAccountingCodeRef()
	{
		if (accountingCodeRef.getRawRef().isInvalid())
			return ORef.createInvalidWithType(AccountingCode.getObjectType());
		
		return accountingCodeRef.getRawRef();
	}
	
	@Override
	public String toString()
	{
		return getLabel();
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	public static int getObjectType()
	{
		return ObjectType.EXPENSE_ASSIGNMENT;
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static ExpenseAssignment find(ObjectManager objectManager, ORef expenseRef)
	{
		return (ExpenseAssignment) objectManager.findObject(expenseRef);
	}
	
	public static ExpenseAssignment find(Project project, ORef expenseRef)
	{
		return find(project.getObjectManager(), expenseRef);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		
		accountingCodeRef = new ORefData(TAG_ACCOUNTING_CODE_REF);
		fundingSourceRef = new ORefData(TAG_FUNDING_SOURCE_REF);
		
		addField(TAG_ACCOUNTING_CODE_REF, accountingCodeRef);
		addField(TAG_FUNDING_SOURCE_REF, fundingSourceRef);
	}
	
	public static final String OBJECT_NAME = "ExpenseAssignment";
	
	public static final String TAG_ACCOUNTING_CODE_REF = "AccountingCodeRef";
	public static final String TAG_FUNDING_SOURCE_REF = "FundingSourceRef";
	
	private ORefData accountingCodeRef;
	private ORefData fundingSourceRef;
}
