/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.conservationmeasures.eam.wizard.WorkPlanWizardStep;

public class WorkPlanAssignResourcesStep extends WorkPlanWizardStep
{
	public WorkPlanAssignResourcesStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_4A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpWorkPlanAssignResourcesStep.class;
	}

	public String getSubHeading()
	{
		return EAM.text("Page 3");
	}
}
