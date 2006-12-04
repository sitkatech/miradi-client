/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringIndicator extends WorkPlanTreeTableNode
{
	public WorkPlanMonitoringIndicator(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
		rebuild();
	}
	
	public EAMObject getObject()
	{
		return indicator;
	}

	public ORef getObjectReference()
	{
		return indicator.getRef();
	}
	
	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.getLabel();
	}

	public int getChildCount()
	{
		return tasks.length;
	}

	public TreeTableNode getChild(int index)
	{
		return tasks[index];
	}

	public Object getValueAt(int column)
	{
		if (column == 0)
			return toString();
		
		//TODO should this be HTML?
		if (column == 1)
			return toString();
		return "";
	}
	
	public boolean canInsertActivityHere()
	{
		return true;
	}
	
	public BaseId getId()
	{
		return indicator.getId();
	}

	public void rebuild()
	{	
		int childCount = indicator.getSubtaskCount();
		Vector taskVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId taskId = indicator.getSubtaskId(i);
			Task task = project.getTaskPool().find(taskId);
			if(task == null)
			{
				EAM.logWarning("Ignoring null activity " + taskId + " in work plan " + indicator.getId());
				continue;
			}
			taskVector.add(new WorkPlanStrategyTask(project, task));
		}
		tasks = (WorkPlanStrategyTask[])taskVector.toArray(new WorkPlanStrategyTask[0]);

	}

	WorkPlanStrategyTask[] tasks;
	Project project;
	Indicator indicator;
}
