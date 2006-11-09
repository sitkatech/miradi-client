/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class MonitoringPlanWizardPanel extends WizardPanel
{
	public MonitoringPlanWizardPanel() throws Exception
	{
		int WELCOME = addStep(new MonitoringPlanWizardWelcomeStep(this));

		setStep(WELCOME);
	}

}
