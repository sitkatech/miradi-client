/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class StratPlanStrategy extends StratPlanObject
{
	public StratPlanStrategy(Project projectToUse, ConceptualModelIntervention interventionToUse)
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

	public Object getChild(int index)
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
	
	public int getId()
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
			int activityId = intervention.getActivityIds().get(i);
			Task activity = project.getTaskPool().find(activityId);
			if(activity == null)
			{
				EAM.logWarning("Ignoring null activity " + activityId + " in intervention " + intervention.getId());
				continue;
			}
			activityVector.add(new StratPlanActivity(activity));
		}
		activities = (StratPlanActivity[])activityVector.toArray(new StratPlanActivity[0]);
	}
	
	Project project;
	ConceptualModelIntervention intervention;
	StratPlanActivity[] activities;
}

