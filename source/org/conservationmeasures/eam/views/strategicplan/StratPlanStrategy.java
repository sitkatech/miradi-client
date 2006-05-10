/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;

public class StratPlanStrategy extends StratPlanObject
{
	public StratPlanStrategy(Project projectToUse, ConceptualModelIntervention interventionToUse)
	{
		project = projectToUse;
		intervention = interventionToUse;
		rebuild();
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
		activities = new StratPlanActivity[childCount];
		for(int i = 0; i < childCount; ++i)
		{
			int activityId = intervention.getActivityIds().get(i);
			activities[i] = new StratPlanActivity(project.getTaskPool().find(activityId));
		}
	}
	
	Project project;
	ConceptualModelIntervention intervention;
	StratPlanActivity[] activities;
}

