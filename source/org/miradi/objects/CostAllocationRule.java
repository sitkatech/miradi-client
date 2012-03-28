/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.utils.EnhancedJsonObject;

public class CostAllocationRule extends BaseObject
{
	public CostAllocationRule(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, new CostAllocationRuleSchema());
		clear();
	}
	
	public CostAllocationRule(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject, new CostAllocationRuleSchema());
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.COST_ALLOCATION_RULE;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static final String TAG_PARENT_REF = "ParentRef";
	public static final String TAG_CHILD_REF = "ChildRef";
	public static final String TAG_COST_PERCENTAGE = "CostPercentage"; 
	
	public static final String OBJECT_NAME = "CostAllocationRule";
}
