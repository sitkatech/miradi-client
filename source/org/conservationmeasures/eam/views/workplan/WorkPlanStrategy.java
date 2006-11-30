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
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategy extends WorkPlanTreeTableNode
{
	public WorkPlanStrategy(Project projectToUse, Strategy interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
		rebuild();
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

	public boolean canInsertActivityHere()
	{
		return true;
	}
	
	public void rebuild()
	{
		int childCount = intervention.getActivityIds().size();
		Vector activityVector = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId activityId = intervention.getActivityIds().get(i);
			Task activity = project.getTaskPool().find(activityId);
			if(activity == null)
			{
				EAM.logWarning("Ignoring null activity " + activityId + " in work plan " + intervention.getId());
				continue;
			}
			activityVector.add(new WorkPlanStrategyTask(project, activity));
		}
		activities = (WorkPlanStrategyTask[])activityVector.toArray(new WorkPlanStrategyTask[0]);
	}

	
	Project project;
	Strategy intervention;
	WorkPlanStrategyTask[] activities;
}

