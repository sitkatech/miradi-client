/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.jump.ActionJumpMonitoringWizardEditIndicatorsStep;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.MonitoringPlanWizardStep;
import org.miradi.wizard.WizardPanel;

public class MonitoringWizardEditIndicatorsStep  extends MonitoringPlanWizardStep
{
	public MonitoringWizardEditIndicatorsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpMonitoringWizardEditIndicatorsStep.class;
	}

}

