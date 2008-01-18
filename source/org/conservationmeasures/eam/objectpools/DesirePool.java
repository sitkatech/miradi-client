/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objects.Desire;

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
