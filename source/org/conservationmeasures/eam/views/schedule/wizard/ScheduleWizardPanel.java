/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.schedule.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class ScheduleWizardPanel extends WizardPanel
{
	public ScheduleWizardPanel() throws Exception
	{
		int WELCOME = addStep(new ScheduleWizardWelcomeStep(this));
		setStep(WELCOME);
	}
}
