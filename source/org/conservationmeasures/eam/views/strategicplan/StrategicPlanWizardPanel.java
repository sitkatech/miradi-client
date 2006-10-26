/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class StrategicPlanWizardPanel extends WizardPanel
{
	public StrategicPlanWizardPanel() throws Exception
	{
		WELCOME = addStep(new StrategicPlanWizardWelcomeStep(this));

		setStep(WELCOME);
	}

	public void jump(Class stepMarker) throws Exception
	{
		throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	private int WELCOME = 0;
}
