/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class StrategicPlanWizardPanel extends WizardPanel
{
	public StrategicPlanWizardPanel() throws Exception
	{
		int WELCOME = addStep(new StrategicPlanWizardWelcomeStep(this));

		setStep(WELCOME);
	}

}
