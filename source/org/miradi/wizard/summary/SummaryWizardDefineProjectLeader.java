/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.summary;

import org.miradi.actions.jump.ActionJumpSummaryWizardDefineProjectLeader;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.SummaryWizardStep;
import org.miradi.wizard.WizardPanel;

public class SummaryWizardDefineProjectLeader extends SummaryWizardStep
{
	public SummaryWizardDefineProjectLeader(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpSummaryWizardDefineProjectLeader.class;
	}
	
}


