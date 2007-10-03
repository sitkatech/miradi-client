/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;

public class ProcessMenu2b extends MiradiMenu
{
	public ProcessMenu2b(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_2B, actions);
		setMnemonic(KeyEvent.VK_S);
		
		addMenuItem(ActionJumpSelectChainStep.class, KeyEvent.VK_I);
		addMenuItem(ActionJumpRankDraftStrategiesStep.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpDiagramWizardResultsChainStep.class, KeyEvent.VK_R);
		addMenuItem(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);
	}
}
