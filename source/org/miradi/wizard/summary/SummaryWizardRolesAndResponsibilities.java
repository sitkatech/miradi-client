/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.summary;

import org.miradi.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.SummaryWizardStep;
import org.miradi.wizard.WizardPanel;

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
	
	@Override
	public String getSubHeading()
	{
		return EAM.text("Other Orgs");
	}
}
