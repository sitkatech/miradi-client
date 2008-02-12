/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.schedule;

import org.miradi.actions.views.ActionViewSchedule;
import org.miradi.views.schedule.ScheduleView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

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
