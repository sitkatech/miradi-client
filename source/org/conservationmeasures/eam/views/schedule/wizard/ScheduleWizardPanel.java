/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.schedule.wizard;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class ScheduleWizardPanel extends WizardPanel
{
	public ScheduleWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		int WELCOME = addStep(new ScheduleWizardWelcomeStep(this));
		setStep(WELCOME);
	}
}
