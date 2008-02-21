/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.threatmatrix;

import org.miradi.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.ThreatRatingWizardStep;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;


public class ThreatRatingWizardCheckTotalsStep extends ThreatRatingWizardStep
{
	public ThreatRatingWizardCheckTotalsStep(WizardPanel panel)
	{
		super(panel);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1C;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpThreatMatrixOverviewStep.class;
	}
	
	public String getSubHeading()
	{
		return EAM.text("Review threat rating summary");
	}
	
	public void buttonPressed(String buttonName)
	{
		if(!isProjectInStressMode() && buttonName.equals(WizardManager.CONTROL_BACK))
			buttonName = END_OF_THREAT_SIMPLE_BRANCH;
		super.buttonPressed(buttonName);
	}
	
	public static final String END_OF_THREAT_SIMPLE_BRANCH = "EndOfThreatSimpleBranch";
}

