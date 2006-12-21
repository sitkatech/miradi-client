/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget.wizard;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class BudgetWizardPanel extends WizardPanel
{
	public BudgetWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		int WELCOME = addStep(new BudgetWizardWelcomeStep(this));
		setStep(WELCOME);
	}
}
