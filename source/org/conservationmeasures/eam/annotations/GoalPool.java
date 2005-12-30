/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.project.ObjectPool;

public class GoalPool extends ObjectPool
{
	public void put(Goal goal)
	{
		put(goal.getId(), goal);
	}
	
	public Goal find(int id)
	{
		return (Goal)getRawObject(id);
	}
}
