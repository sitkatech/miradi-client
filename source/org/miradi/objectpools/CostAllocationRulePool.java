/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.CostAllocationRule;
import org.miradi.project.ObjectManager;

public class CostAllocationRulePool extends EAMNormalObjectPool
{
	public CostAllocationRulePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.COST_ALLOCATION_RULE);
	}
	
	public void put(CostAllocationRule costAllocationRule)
	{
		put(costAllocationRule.getId(), costAllocationRule);
	}
	
	public CostAllocationRule find(BaseId id)
	{
		return (CostAllocationRule) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new CostAllocationRule(objectManager, new BaseId(actualId.asInt()));
	}
}
