/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objects.BaseObject;

abstract public class EAMNormalObjectPool extends PoolWithIdAssigner
{
	public EAMNormalObjectPool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(objectTypeToStore, idAssignerToUse);
	}
	
	public BaseObject createObject(BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId actualId = idAssigner.obtainRealId(objectId);
		BaseObject created = createRawObject(actualId, extraInfo);
		put(actualId, created);
		return created;
	}
	
	abstract BaseObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception;
}
