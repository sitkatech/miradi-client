/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewPanel extends WizardPanel
{
	public InterviewPanel() throws Exception
	{
		steps = new InterviewWizardStep[STEP_COUNT];
		steps[WELCOME] = new InterviewWizardWelcomeStep(this);
		steps[TEMPORARY_GUIDE] = new InterviewWizardTemporaryGuideStep(this);
		steps[DEFINE_SCOPE_A] = new InterviewWizardDefineScopeAStep(this);
		steps[DEFINE_SCOPE_B] = new InterviewWizardDefineScopeBStep(this);
		steps[DEVELOP_OBJECTIVES_A] = new InterviewWizardDevelopObjectivesAStep(this);
		steps[DEVELOP_OBJECTIVES_B] = new InterviewWizardDevelopObjectivesBStep(this);

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
	

	private static final int WELCOME = 0;
	private static final int TEMPORARY_GUIDE = 1;
	private static final int DEFINE_SCOPE_A = 2;
	private static final int DEFINE_SCOPE_B = 3;
	private static final int DEVELOP_OBJECTIVES_A = 4;
	private static final int DEVELOP_OBJECTIVES_B = 5;
	
	private static final int STEP_COUNT = 6;
	
	InterviewWizardStep[] steps;
	int currentStep;

}
