/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class StrategicPlanWizardPanel extends WizardPanel
{
	public StrategicPlanWizardPanel() throws Exception
	{
		steps = new WizardStep[STEP_COUNT];
		steps[WELCOME] = new StrategicPlanWizardWelcomeStep(this);

		setStep(WELCOME);
	}

	public void next() throws Exception
	{
		int nextStep = currentStep + 1;
		if(nextStep >= steps.length)
			nextStep = 0;
		
		setStep(nextStep);
	}
	
	public void previous() throws Exception
	{
		int nextStep = currentStep - 1;
		if(nextStep < 0)
			return;
		
		setStep(nextStep);
	}
	
	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		steps[currentStep].refresh();
		setContents(steps[currentStep]);
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	private static final int WELCOME = 0;
	
	private static final int STEP_COUNT = 1;
	
	WizardStep[] steps;
	int currentStep;


}
