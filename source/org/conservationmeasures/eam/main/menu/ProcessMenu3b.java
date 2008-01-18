/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopFundingProposals;
import org.conservationmeasures.eam.actions.jump.ActionJumpObtainFinancing;

public class ProcessMenu3b extends MiradiMenu
{
	public ProcessMenu3b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_3B, actions);
		setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(ActionJumpBudgetWizardAccountingAndFunding.class, KeyEvent.VK_E);
		addMenuItem(ActionJumpDevelopFundingProposals.class, KeyEvent.VK_D);
		addMenuItem(ActionJumpObtainFinancing.class, KeyEvent.VK_O);
	}
}
