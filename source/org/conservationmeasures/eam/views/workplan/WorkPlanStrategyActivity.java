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
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategyActivity extends WorkPlanTreeTableNode
{
	public WorkPlanStrategyActivity(Project projectToUse, Task activityToUse)
	{
		project = projectToUse;
		
		if(activityToUse == null)
			EAM.logError("Attempted to create tree node for null activity");
		activity = activityToUse;
		rebuild();
	}
	
	public Object getValueAt(int column)
	{
		if(column == 0)
			return toString();
		if(column == 1)
			return getResourcesAsHtml();
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
		return activity.toString();
	}
	
	public Task getActivity()
	{
		return activity;
	}

	public ORef getObjectReference()
	{
		return activity.getObjectReference();
	}
	
	public int getType()
	{
		return activity.getType();
	}
	
	public BaseId getId()
	{
		return activity.getId();
	}
	
	String getResourcesAsHtml()
	{
		return EAMBaseObject.toHtml(project.getTaskResources(activity));
	}
	
	public boolean canInsertActivityHere()
	{
		return true;
	}
	
	public boolean canInsertTaskHere()
	{
		return false;
	}

	public void rebuild()
	{
		IdList taskIdList = activity.getSubtaskIdList();
		Vector possibleTasks = new Vector();
		for (int i = 0; i < taskIdList.size(); i++)
		{
			Task task = project.getTaskPool().find(taskIdList.get(i));
			possibleTasks.add(new WorkPlanStrategyTask(project, task));
		}
		
		tasks = (WorkPlanStrategyTask[])possibleTasks.toArray(new WorkPlanStrategyTask[0]);
		Arrays.sort(tasks, new IgnoreCaseStringComparator());
	}

	Project project;
	Task activity;
	WorkPlanStrategyTask[] tasks;
}
