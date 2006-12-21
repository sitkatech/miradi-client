/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan.wizard;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class WorkPlanWizardPanel extends WizardPanel
{
	public WorkPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		int WELCOME = addStep(new WorkPlanWizardWelcomeStep(this));
		setStep(WELCOME);
	}
}
