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
import org.miradi.objects.IntermediateResult;
import org.miradi.project.ObjectManager;

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
