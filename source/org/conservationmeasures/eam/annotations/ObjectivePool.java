/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.project.ObjectPool;

public class ObjectivePool extends ObjectPool
{
	public void put(Objective objective)
	{
		put(objective.getId(), objective);
	}
	
	public Objective find(int id)
	{
		return (Objective)getRawObject(id);
	}

}
