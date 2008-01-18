/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.planning;

import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.conservationmeasures.eam.wizard.WorkPlanWizardStep;

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
