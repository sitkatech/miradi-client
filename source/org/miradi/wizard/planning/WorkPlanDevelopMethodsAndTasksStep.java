/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WorkPlanWizardStep;

public class WorkPlanDevelopMethodsAndTasksStep extends WorkPlanWizardStep
{
	public WorkPlanDevelopMethodsAndTasksStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpWorkPlanDevelopMethodsAndTasksStep.class;
	}

	public String getSubHeading()
	{
		return EAM.text("Page 1");
	}
}
