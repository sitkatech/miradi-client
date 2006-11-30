/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;

public class TaskPool extends EAMNormalObjectPool
{
	public TaskPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TASK);
	}
	
	public void put(Task task)
	{
		put(task.getId(), task);
	}
	
	public Task find(BaseId id)
	{
		return (Task)getRawObject(id);
	}

	EAMObject createRawObject(BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Task(actualId, (CreateTaskParameter)extraInfo);
	}


}
