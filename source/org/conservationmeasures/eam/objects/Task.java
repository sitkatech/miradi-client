/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.Vector;

public class Task extends EAMObject
{
	public Task(int idToUse)
	{
		super(idToUse);
		subtasks = new Vector();
	}

	public int getType()
	{
		return ObjectType.TASK;
	}

	public void addSubtask(Task newChild)
	{
		subtasks.add(newChild);
	}
	
	public int getSubtaskCount()
	{
		return subtasks.size();
	}
	
	public Task getSubtask(int index)
	{
		return (Task)subtasks.get(index);
	}
	
	Vector subtasks;
}
