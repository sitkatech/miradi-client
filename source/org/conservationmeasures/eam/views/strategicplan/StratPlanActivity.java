/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.annotations.ResourcePool;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class StratPlanActivity extends StratPlanObject
{
	public StratPlanActivity(Project projectToUse, Task activityToUse)
	{
		project = projectToUse;
		
		if(activityToUse == null)
			EAM.logError("Attempted to create tree node for null activity");
		activity = activityToUse;
	}
	
	public Object getValueAt(int column)
	{
		if(column == StrategicPlanTreeTableModel.labelColumn)
			return toString();
		if(column == StrategicPlanTreeTableModel.resourcesColumn)
			return getResourcesAsString();
		return "";
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
	
	public boolean canInsertActivityHere()
	{
		return false;
	}
	
	public void rebuild()
	{
		
	}
	
	String getResourcesAsString()
	{
		StringBuffer result = new StringBuffer();
		ResourcePool pool = project.getResourcePool();
		IdList resourceIds = activity.getResourceIdList();
		for(int i = 0; i < resourceIds.size(); ++i)
		{
			if(i > 0)
				result.append(", ");
			ProjectResource resource = pool.find(resourceIds.get(i));
			result.append(resource.toString());
		}
		
		return result.toString();
	}
	
	Project project;
	Task activity;
}
