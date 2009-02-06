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
import org.miradi.actions.jump.ActionJumpAssessStakeholders;
import org.miradi.actions.jump.ActionJumpDiagramWizardCreateInitialModelStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.miradi.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;

public class ProcessMenu1d extends MiradiMenu
{

	public ProcessMenu1d(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1D, actionsToUse);
		setMnemonic(KeyEvent.VK_M);
		
		addMenuItem(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpAssessStakeholders.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpDiagramWizardCreateInitialModelStep.class, KeyEvent.VK_C); 
//		addMenuItem(ActionJumpAnalyzeProjectCapacity.class, KeyEvent.VK_C);
		
//		addMenuItem(ActionJumpArticulateCoreAssumptions.class, KeyEvent.VK_A);
		addMenuItem(ActionJumpDiagramWizardReviewModelAndAdjustStep.class, KeyEvent.VK_R);
//		addMenuItem(ActionJumpGroundTruthRevise.class, KeyEvent.VK_G);
	}

}
