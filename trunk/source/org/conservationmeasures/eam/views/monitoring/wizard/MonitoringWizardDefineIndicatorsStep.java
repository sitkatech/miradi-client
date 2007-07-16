/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.main.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class MonitoringWizardDefineIndicatorsStep extends DiagramWizardStep
{
	public MonitoringWizardDefineIndicatorsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpMonitoringWizardDefineIndicatorsStep.class;
	}

}

