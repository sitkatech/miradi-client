/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class MonitoringWizardFocusStep extends DiagramWizardStep
{
	public MonitoringWizardFocusStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_2B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpMonitoringWizardFocusStep.class;
	}

}

