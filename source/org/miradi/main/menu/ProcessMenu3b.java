/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.miradi.actions.jump.ActionJumpDevelopFundingProposals;
import org.miradi.actions.jump.ActionJumpObtainFinancing;

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
