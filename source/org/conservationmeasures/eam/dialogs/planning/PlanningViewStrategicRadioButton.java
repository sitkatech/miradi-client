/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewStrategicRadioButton extends PlanningViewRadioButton
{
	public PlanningViewStrategicRadioButton(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public String getPropertyName()
	{
		return PlanningView.STRATEGIC_PLAN_RADIO_CHOICE;
	}
}
