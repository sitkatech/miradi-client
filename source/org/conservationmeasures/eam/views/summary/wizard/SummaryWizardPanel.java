/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class SummaryWizardPanel extends WizardPanel
{
	public SummaryWizardPanel(Actions actionsToUse) throws Exception
	{
		actions = actionsToUse;
		
		WELCOME = addStep(new SummaryWizardOverviewStep(this));

		setStep(WELCOME);
	}

	public void next() throws Exception
	{
		actions.get(ActionJumpCreateModel.class).doAction();
	}

	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpSelectTeam.class))
			setStep(WELCOME);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	Actions actions;
	int WELCOME;
}
