/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategyNode extends TreeTableNode
{
	public WorkPlanStrategyNode(Project projectToUse, Strategy interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return intervention;
	}

	public Strategy getIntervention()
	{
		return intervention;
	}

	public Object getValueAt(int column)
	{
		if (column == 0)
			return intervention.getLabel();
		return "";
	}

	public int getChildCount()
	{
		return activities.length;
	}

	public TreeTableNode getChild(int index)
	{
		return activities[index];
	}

	public String toString()
	{
		return intervention.toString();
	}
	
	public ORef getObjectReference()
	{
		return intervention.getRef();
	}

	public int getType()
	{
		return intervention.getType();
	}

	public BaseId getId()
	{
		return intervention.getId();
	}

	public void rebuild()
	{
		activities = getWorkPlanActivitiesTask(intervention);
	}

	static public WorkPlanTaskNode[] getWorkPlanActivitiesTask(Strategy intervention)
	{
		Project project = intervention.getProject();
		int childCount = intervention.getActivityIds().size();
		Vector activityVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId activityId = intervention.getActivityIds().get(i);
			Task activity = project.getTaskPool().find(activityId);
			activityVector.add(new WorkPlanTaskNode(project, activity));
		}
		WorkPlanTaskNode[] activities = (WorkPlanTaskNode[])activityVector.toArray(new WorkPlanTaskNode[0]);
		return activities;
	}

	
	Project project;
	Strategy intervention;
	WorkPlanTaskNode[] activities;
}

