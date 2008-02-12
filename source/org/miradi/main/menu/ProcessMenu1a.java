/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.miradi.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;

public class ProcessMenu1a extends MiradiMenu
{
	public ProcessMenu1a(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1A, actionsToUse);
		setMnemonic(KeyEvent.VK_D);

		addMenuItem(ActionJumpSummaryWizardDefineTeamMembers.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpSummaryWizardRolesAndResponsibilities.class, KeyEvent.VK_R);

	}

}
