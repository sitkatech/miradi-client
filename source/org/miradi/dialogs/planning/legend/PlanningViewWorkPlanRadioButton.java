/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.legend;

import org.miradi.project.Project;
import org.miradi.views.planning.PlanningView;

public class PlanningViewWorkPlanRadioButton extends PlanningViewRadioButton
{
	public PlanningViewWorkPlanRadioButton(Project projectToUse)
	{
		super(projectToUse);
	}
		
	public String getPropertyName()
	{
		return PlanningView.WORKPLAN_PLAN_RADIO_CHOICE;
	}
}
