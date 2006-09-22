/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Goal;

public class GoalPool extends EAMObjectPool
{
	public void put(Goal goal)
	{
		put(goal.getId(), goal);
	}
	
	public Goal find(BaseId id)
	{
		return (Goal)getRawObject(id);
	}

}
