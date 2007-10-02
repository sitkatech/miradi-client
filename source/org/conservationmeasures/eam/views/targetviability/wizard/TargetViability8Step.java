/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.targetviability.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class TargetViability8Step extends DiagramWizardStep
{
	public TargetViability8Step(WizardPanel wizardToUse)
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
		return EAM.text("Page 7");
	}
}
