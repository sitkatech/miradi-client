/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class WorkPlanStrategyActivity extends WorkPlanNode
{
	public WorkPlanStrategyActivity(Project projectToUse, Task activityToUse)
	{
		project = projectToUse;
		
		if(activityToUse == null)
			EAM.logError("Attempted to create tree node for null activity");
		activity = activityToUse;
	}
	
	public Object getValueAt(int column)
	{
			return getResourcesAsHtml();
	}

	public int getChildCount()
	{
		return 0;
	}

	public Object getChild(int index)
	{
		return null;
	}
	
	public String toString()
	{
		return activity.getLabel();
	}
	
	public Task getActivity()
	{
		return activity;
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

	Project project;
	Task activity;
}
