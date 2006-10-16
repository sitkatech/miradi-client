/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objects.EAMObject;

abstract public class EAMNormalObjectPool extends EAMObjectPool
{
	public EAMNormalObjectPool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(objectTypeToStore);
		idAssigner = idAssignerToUse;
	}
	
	public EAMObject createObject(BaseId proposedId)
	{
		BaseId actualId = idAssigner.obtainRealId(proposedId);
		EAMObject created = createRawObject(actualId);
		put(actualId, created);
		return created;
	}
	
	abstract EAMObject createRawObject(BaseId actualId);
	
	public void put(BaseId id, Object obj)
	{
		super.put(id, obj);
		idAssigner.idTaken(id);
	}

	IdAssigner idAssigner;
}
