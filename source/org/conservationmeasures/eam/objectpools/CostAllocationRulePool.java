/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.CostAllocationRule;
import org.conservationmeasures.eam.project.ObjectManager;

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
