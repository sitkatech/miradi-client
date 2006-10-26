/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel() throws Exception
	{
		int WELCOME = addStep(new SummaryWizardWelcomeStep(this));
		addStep(new InterviewWizardTemporaryGuideStep(this));
		addStep(new InterviewWizardDefineScopeAStep(this));
		addStep(new InterviewWizardDefineScopeBStep(this));
		addStep(new InterviewWizardDevelopObjectivesAStep(this));
		addStep(new InterviewWizardDevelopObjectivesBStep(this));

		setStep(WELCOME);
	}

	
}
