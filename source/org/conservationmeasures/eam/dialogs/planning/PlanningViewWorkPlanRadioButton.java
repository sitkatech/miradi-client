/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewWorkPlanRadioButton extends PlanningViewRadioButton
{
	public PlanningViewWorkPlanRadioButton(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public String[] getColumnList()
	{
		return new String[] {Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
							 Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
							 Task.PSEUDO_TAG_TASK_TOTAL, };
	}

	public String[] getRowList()
	{
		return new String[] {Strategy.OBJECT_NAME,
							Task.ACTIVITY_NAME,
							Indicator.OBJECT_NAME,
							Task.METHOD_NAME,
							Task.OBJECT_NAME, };
	}
	
	public String getPropertyName()
	{
		return PlanningView.WORKPLAN_PLAN_RADIO_CHOICE;
	}
}
