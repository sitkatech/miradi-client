/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;

public class ProcessMenu1a extends MiradiMenu
{
	public ProcessMenu1a(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1A, actionsToUse);
		setMnemonic(KeyEvent.VK_D);

		addMenuItem(ActionJumpSummaryWizardDefineTeamMembers.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpSummaryWizardRolesAndResponsibilities.class, KeyEvent.VK_C);

	}

}
