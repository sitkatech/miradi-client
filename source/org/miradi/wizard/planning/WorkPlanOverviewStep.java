/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.views.ActionViewWorkPlan;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WorkPlanWizardStep;

public class WorkPlanOverviewStep extends WorkPlanWizardStep
{
	public WorkPlanOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewWorkPlan.class;
	}
}
