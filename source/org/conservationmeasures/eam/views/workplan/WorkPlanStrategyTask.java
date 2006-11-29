/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategyTask extends WorkPlanTreeTableNode
{
	public WorkPlanStrategyTask(Project projectToUse, Task taskToUse)
	{
		project = projectToUse;
		currentTask = taskToUse;
		rebuild();
	}
	
	public BaseId getId()
	{
		return getObjectReference().getObjectId();
	}

	public TreeTableNode getChild(int index)
	{
		return tasks[index];
	}

	public int getChildCount()
	{
		return tasks.length;
	}

	public ORef getObjectReference()
	{
		return currentTask.getObjectReference();
	}

	public int getType()
	{
		return getObjectReference().getObjectType();
	}

	public Object getValueAt(int index)
	{
		return toString();
	}

	public String toString()
	{
		return currentTask.getLabel();
	}
	
	public void rebuild()
	{
		IdList taskIdList = currentTask.getSubtaskIdList();
		Vector possibleTasks = new Vector();
		for (int i = 0; i < taskIdList.size(); i++)
		{
			Task task = project.getTaskPool().find(taskIdList.get(i));
			possibleTasks.add(new WorkPlanStrategyTask(project, task));
		}
		//TODO sort nodes in tree
		tasks = (WorkPlanStrategyTask[])possibleTasks.toArray(new WorkPlanStrategyTask[0]);
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public boolean canInsertTaskHere()
	{
		return true;
	}
	
	Project project;
	Task currentTask;
	WorkPlanStrategyTask[] tasks;
}
