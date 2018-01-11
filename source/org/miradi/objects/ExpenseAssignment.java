/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.*;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.utils.OptionalDouble;

public class ExpenseAssignment extends Assignment
{
	public ExpenseAssignment(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static ExpenseAssignmentSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ExpenseAssignmentSchema createSchema(ObjectManager objectManager)
	{
		return (ExpenseAssignmentSchema) objectManager.getSchemas().get(ObjectType.EXPENSE_ASSIGNMENT);
	}
		
	@Override
	protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity)
	{
		return new TimePeriodCosts(getFundingSourceRef(), getAccountingCodeRef(), getCategoryOneRef(), getCategoryTwoRef(), quantity);
	}
	
	@Override
	protected String getFundingSourceTag()
	{
		return TAG_FUNDING_SOURCE_REF;
	}

	@Override
	protected String getAccountingCodeTag()
	{
		return TAG_ACCOUNTING_CODE_REF;
	}
	
	@Override
	public ORef getFundingSourceRef()
	{
		ORef ref = getRefData(TAG_FUNDING_SOURCE_REF);
		if (ref.isInvalid())
			return ORef.createInvalidWithType(FundingSourceSchema.getObjectType());
		
		return ref;
	}
	
	@Override
	public ORef getAccountingCodeRef()
	{
		ORef ref = getRefData(TAG_ACCOUNTING_CODE_REF);
		if (ref.isInvalid())
			return ORef.createInvalidWithType(AccountingCodeSchema.getObjectType());
		
		return ref;
	}
	
	@Override
	public TimePeriodCostsMap getTotalTimePeriodCostsMapForAssignments() throws Exception
	{
		return getTimePeriodCostsMap(TAG_EXPENSE_ASSIGNMENT_REFS);
	}
	
	@Override
	public String toString()
	{
		return getLabel();
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ExpenseAssignmentSchema.getObjectType();
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
	
	public static final String TAG_ACCOUNTING_CODE_REF = "AccountingCodeRef";
	public static final String TAG_FUNDING_SOURCE_REF = "FundingSourceRef";
}
