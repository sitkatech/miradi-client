/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.project.ObjectManager;

public class IntermediateResultPool extends EAMNormalObjectPool
{
	public IntermediateResultPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.INTERMEDIATE_RESULT);
	}
	
	public void put(IntermediateResult intermediateResult)
	{
		put(intermediateResult.getId(), intermediateResult);
	}
	
	public IntermediateResult find(BaseId id)
	{
		return (IntermediateResult)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new IntermediateResult(objectManager ,new FactorId(actualId.asInt()));
	}
}
