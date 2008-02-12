/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.ObjectManager;

public class ThreatReductionResultPool extends EAMNormalObjectPool
{
	public ThreatReductionResultPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.THREAT_REDUCTION_RESULT);
	}
	
	public void put(ThreatReductionResult threatReduction)
	{
		put(threatReduction.getId(), threatReduction);
	}
	
	public ThreatReductionResult find(BaseId id)
	{
		return (ThreatReductionResult)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new ThreatReductionResult(objectManager ,new FactorId(actualId.asInt()));
	}
}
