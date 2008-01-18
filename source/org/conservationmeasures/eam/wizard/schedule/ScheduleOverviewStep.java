/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.schedule;

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
