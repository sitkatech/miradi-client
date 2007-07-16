/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.project.ObjectManager;

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
