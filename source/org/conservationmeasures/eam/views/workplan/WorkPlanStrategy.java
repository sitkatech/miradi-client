/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanStrategy extends WorkPlanTreeTableNode
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
	
	public ObjectReference getObjectReference()
	{
		return intervention.getObjectReference();
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
			activityVector.add(new WorkPlanStrategyActivity(project, activity));
		}
		activities = (WorkPlanStrategyActivity[])activityVector.toArray(new WorkPlanStrategyActivity[0]);
		Arrays.sort(activities, new IgnoreCaseStringComparator());
	}

	
	Project project;
	ConceptualModelIntervention intervention;
	WorkPlanStrategyActivity[] activities;
}

