/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
