/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategy extends WorkPlanNode
{
	public WorkPlanStrategy(Project projectToUse, ConceptualModelIntervention interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
		rebuild();
	}

	public ConceptualModelIntervention getIntervention()
	{
		return intervention;
	}

	public Object getValueAt(int column)
	{
		return intervention.getLabel();
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
		return intervention.getLabel();
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
		return false;
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
			activityVector.add(new WorkPlanStrategyActivity(project, activity));
		}
		activities = (WorkPlanStrategyActivity[])activityVector.toArray(new WorkPlanStrategyActivity[0]);
	}

	Project project;
	ConceptualModelIntervention intervention;
	WorkPlanStrategyActivity[] activities;
}

