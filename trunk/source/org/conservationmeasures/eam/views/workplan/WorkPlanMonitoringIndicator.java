/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringIndicator extends TreeTableNode
{
	public WorkPlanMonitoringIndicator(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
		rebuild();
	}
	
	public BaseObject getObject()
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
		return indicator.toString();
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
		
		if (column == 1)
			return toString();
		return "";
	}
	
	public BaseId getId()
	{
		return indicator.getId();
	}

	public void rebuild()
	{	
		tasks = getWorkPlanIndicatorTask(indicator);
	}

	public static WorkPlanTaskNode[] getWorkPlanIndicatorTask(Indicator indicator)
	{
		Project project = indicator.getProject();
		int childCount = indicator.getTaskCount();
		Vector taskVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId taskId = indicator.getTaskId(i);
			Task task = project.getTaskPool().find(taskId);
			if(task == null)
			{
				EAM.logWarning("Ignoring null activity " + taskId + " in work plan " + indicator.getId());
				continue;
			}
			taskVector.add(new WorkPlanTaskNode(project, task));
		}
		WorkPlanTaskNode[] tasks = (WorkPlanTaskNode[])taskVector.toArray(new WorkPlanTaskNode[0]);
		return tasks;
	}

	WorkPlanTaskNode[] tasks;
	Project project;
	Indicator indicator;
}
