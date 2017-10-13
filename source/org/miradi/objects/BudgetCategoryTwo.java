/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BudgetCategoryTwoSchema;

public class BudgetCategoryTwo extends AbstractBudgetCategoryObject
{
	public BudgetCategoryTwo(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static BudgetCategoryTwoSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static BudgetCategoryTwoSchema createSchema(ObjectManager objectManager)
	{
		return (BudgetCategoryTwoSchema) objectManager.getSchemas().get(ObjectType.BUDGET_CATEGORY_TWO);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public String toString()
	{
		return toString(EAM.text("Label|Budget Category Two"));
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == BudgetCategoryTwoSchema.getObjectType();
	}
	
	public static BudgetCategoryTwo find(ObjectManager objectManager, ORef ref)
	{
		return (BudgetCategoryTwo) objectManager.findObject(ref);
	}
	
	public static BudgetCategoryTwo find(Project project, ORef ref)
	{
		return find(project.getObjectManager(), ref);
	}
}
