/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel() throws Exception
	{
		steps = new WizardStep[STEP_COUNT];
		steps[WELCOME] = new SummaryWizardWelcomeStep(this);
		
		setStep(WELCOME);
	}

	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		steps[currentStep].refresh();
		setContents(steps[currentStep]);
	}
	
	private static final int STEP_COUNT = 1;
	private static final int WELCOME = 0;

	WizardStep[] steps;
	int currentStep;
}
