/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Desire;

public class DesirePool extends EAMObjectPool
{
	public DesirePool(int objectTypeToStore)
	{
		super(objectTypeToStore);
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
