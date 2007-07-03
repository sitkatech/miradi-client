/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.schedule.wizard;

import org.conservationmeasures.eam.actions.views.ActionViewSchedule;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.wizard.SplitWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class ScheduleOverviewStep extends SplitWizardStep
{
	public ScheduleOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, ScheduleView.getViewName());
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewSchedule.class;
	}
}
