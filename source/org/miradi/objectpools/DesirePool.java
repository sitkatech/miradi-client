/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objects.Desire;

abstract public class DesirePool extends EAMNormalObjectPool
{
	public DesirePool(IdAssigner idAssignerToUse, int objectTypeToStore)
	{
		super(idAssignerToUse, objectTypeToStore);
	}

	public void put(Desire desire)
	{
		put(desire.getId(), desire);
	}
	
	public Desire findDesire(BaseId id)
	{
		return (Desire)getRawObject(id);
	}

}
