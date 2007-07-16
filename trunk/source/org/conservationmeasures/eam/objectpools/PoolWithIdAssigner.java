/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
