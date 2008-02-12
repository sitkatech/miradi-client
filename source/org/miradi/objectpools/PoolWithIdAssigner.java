/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;

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
