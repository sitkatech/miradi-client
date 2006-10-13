/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.main.EAM;
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
		EAM.logDebug("EAMNormalObjectPool.createObject requested: " + proposedId + " and took " + actualId);
		EAMObject created = createRawObject(actualId);
		put(actualId, created);
		return created;
	}
	
	abstract EAMObject createRawObject(BaseId actualId);
	
	IdAssigner idAssigner;
}
