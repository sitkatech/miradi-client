/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanTaskNode extends TreeTableNode
{
	public WorkPlanTaskNode(Project projectToUse, Task taskToUse)
	{
		project = projectToUse;
		
		if(taskToUse == null)
			EAM.logError("Attempted to create tree node for null activity");
		task = taskToUse;
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return task;
	}
	
	public Object getValueAt(int column)
	{
		if(column == 0)
			return toString();
		return "";
	}

	public int getChildCount()
	{
		return tasks.length;
	}

	public TreeTableNode getChild(int index)
	{
		return tasks[index];
	}
	
	public String toString()
	{
		return task.toString();
	}
	
	public Task getActivity()
	{
		return task;
	}

	public ORef getObjectReference()
	{
		return task.getRef();
	}
	
	public int getType()
	{
		return task.getType();
	}
	
	public BaseId getId()
	{
		return task.getId();
	}
	
	public void rebuild()
	{
		IdList taskIdList = task.getSubtaskIdList();
		Vector possibleTasks = new Vector();
		for (int i = 0; i < taskIdList.size(); i++)
		{
			Task taskFromPool = project.getTaskPool().find(taskIdList.get(i));
			possibleTasks.add(new WorkPlanTaskNode(project, taskFromPool));
		}
		
		tasks = (WorkPlanTaskNode[])possibleTasks.toArray(new WorkPlanTaskNode[0]);
		Arrays.sort(tasks, new IgnoreCaseStringComparator());
	}

	Project project;
	Task task;
	WorkPlanTaskNode[] tasks;
}
