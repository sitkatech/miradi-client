/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllObjectives;
import org.conservationmeasures.eam.main.ProcessSteps;
import org.conservationmeasures.eam.wizard.StrategicPlanWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class StrategicPlanViewAllObjectives extends StrategicPlanWizardStep
{
	public StrategicPlanViewAllObjectives(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_2A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpStrategicPlanViewAllObjectives.class;
	}
}
