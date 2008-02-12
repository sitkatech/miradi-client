/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.project.ObjectManager;

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

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Task(objectManager, actualId);
	}

	public BaseObject[] getAllTasks()
	{
		BaseId[] allIds = getIds();
		Task[] allTasks = new Task[allIds.length];
		for (int i = 0; i < allTasks.length; i++)
			allTasks[i] = find(allIds[i]);
			
		return allTasks;
	}

}
