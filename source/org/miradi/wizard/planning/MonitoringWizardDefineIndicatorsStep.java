/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

public class MonitoringWizardDefineIndicatorsStep extends DiagramWizardStep
{
	public MonitoringWizardDefineIndicatorsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_2B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpMonitoringWizardDefineIndicatorsStep.class;
	}

}

