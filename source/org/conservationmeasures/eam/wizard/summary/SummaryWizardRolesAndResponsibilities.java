/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.summary;

import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.SummaryWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class SummaryWizardRolesAndResponsibilities extends SummaryWizardStep
{
	public SummaryWizardRolesAndResponsibilities(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpSummaryWizardRolesAndResponsibilities.class;
	}
}
