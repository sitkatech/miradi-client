/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.OptionalDouble;

abstract public class Assignment extends AbstractPlanningObject
{
	public Assignment(ObjectManager objectManagerToUse, BaseId idToUse, final BaseObjectSchema schema)
	{
		super(objectManagerToUse, idToUse, schema);
	}

	public ORef getCategoryRef(int categoryObjectType)
	{
		String tagForCategoryType = getTagForCategoryType(categoryObjectType);
		if (tagForCategoryType == null)
			return ORef.INVALID;
		
		ORef categoryRef = getRefData(tagForCategoryType);
		if (categoryRef.isValid())
			return categoryRef;
		
		return ORef.createInvalidWithType(categoryObjectType);
	}

	protected String getTagForCategoryType(int categoryObjectType)
	{
		if (ProjectResource.is(categoryObjectType))
			return getProjectResourceTag();
		
		if (FundingSource.is(categoryObjectType))
			return getFundingSourceTag();
		
		if (AccountingCode.is(categoryObjectType))
			return getAccountingCodeTag();
		
		if (BudgetCategoryOne.is(categoryObjectType))
			return TAG_CATEGORY_ONE_REF;
		
		if (BudgetCategoryTwo.is(categoryObjectType))
			return TAG_CATEGORY_TWO_REF;
		
		throw new RuntimeException("category type was not recognized.  category Object type: "+ categoryObjectType);
	}
	
	protected String getProjectResourceTag()
	{
		return null;
	}

	public boolean hasCategoryData()
	{
		if (getFundingSourceRef().isValid())
			return true;
		
		if (getAccountingCodeRef().isValid())
			return true;
		
		return false;
	}
	
	public ORef getCategoryOneRef()
	{
		return getRef(TAG_CATEGORY_ONE_REF);
	}
	
	public ORef getCategoryTwoRef()
	{
		return getRef(TAG_CATEGORY_TWO_REF);
	}

	public boolean isPartOfASharedTaskTree()
	{
		Factor owner = getDirectOrIndirectOwningFactor();
		if (isSharedTask(owner))
			return true;

		return false;
	}

	public static boolean isAssignment(ORef ref)
	{
		return isAssignment(ref.getObjectType());
	}
	
	public static boolean is(ORef ref)
	{
		return isAssignment(ref.getObjectType());
	}

	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}

	public static boolean isAssignment(BaseObject baseObject)
	{
		return isAssignment(baseObject.getType());
	}
	
	public static boolean isAssignment(int objectType)
	{
		if (ResourceAssignment.is(objectType))
			return true;
		
		return ExpenseAssignment.is(objectType);
	}
	
	public static Assignment findAssignment(ObjectManager objectManager, ORef assignmentRef)
	{
		return (Assignment) find(objectManager, assignmentRef);
	}
	
	public static Assignment findAssignment(Project project, ORef assignmentRef)
	{
		return findAssignment(project.getObjectManager(), assignmentRef);
	}
	
	abstract protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity);
	
	abstract public ORef getFundingSourceRef();
	
	abstract public ORef getAccountingCodeRef();

	abstract protected String getFundingSourceTag();
	
	abstract protected String getAccountingCodeTag();
	
	public static final String TAG_CATEGORY_ONE_REF = "CategoryOneRef";
	public static final String TAG_CATEGORY_TWO_REF = "CategoryTwoRef";
}
