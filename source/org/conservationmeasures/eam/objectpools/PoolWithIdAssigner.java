/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;

public class PoolWithIdAssigner extends EAMObjectPool
{
	public PoolWithIdAssigner(int objectTypeToStore, IdAssigner idAssignerToUse)
	{
		super(objectTypeToStore);
		idAssigner = idAssignerToUse;
	}

	public void put(BaseId id, Object obj)
	{
		super.put(id, obj);
		idAssigner.idTaken(id);
	}

	IdAssigner idAssigner;
}
