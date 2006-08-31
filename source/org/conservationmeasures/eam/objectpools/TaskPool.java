/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Task;

public class TaskPool extends EAMObjectPool
{
	public void put(Task task)
	{
		put(task.getId(), task);
	}
	
	public Task find(BaseId id)
	{
		return (Task)getRawObject(id);
	}


}
