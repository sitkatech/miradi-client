/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.targetviability;

import org.miradi.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.TargetViabilityWizardStep;
import org.miradi.wizard.WizardPanel;

public class TargetViability4Step extends TargetViabilityWizardStep
{
	public TargetViability4Step(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpTargetViabilityMethodChoiceStep.class;
	}
	
	public String getSubHeading()
	{
		return EAM.text("2) Identify indicators");
	}
}
