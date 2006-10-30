/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel() throws Exception
	{
		WELCOME = addStep(new SummaryWizardWelcomeStep(this));
		addStep(new InterviewWizardTemporaryGuideStep(this));
		addStep(new InterviewWizardDefineScopeAStep(this));
		addStep(new InterviewWizardDefineScopeBStep(this));
		addStep(new InterviewWizardDevelopObjectivesAStep(this));
		addStep(new InterviewWizardDevelopObjectivesBStep(this));

		setStep(WELCOME);
	}

	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpSelectTeam.class))
			setStep(WELCOME);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	int WELCOME;
}
