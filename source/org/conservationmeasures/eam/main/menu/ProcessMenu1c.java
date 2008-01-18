/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatMatrixOverviewStep;

public class ProcessMenu1c extends MiradiMenu
{
	public ProcessMenu1c(Actions actionsToUse)
	{
		super(ProcessSteps.PROCESS_STEP_1C, actionsToUse);
		setMnemonic(KeyEvent.VK_U);
		
		addMenuItem(ActionJumpDiagramWizardIdentifyDirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpThreatMatrixOverviewStep.class, KeyEvent.VK_R);
	}

}
