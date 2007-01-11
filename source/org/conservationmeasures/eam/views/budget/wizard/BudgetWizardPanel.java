/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpScheduleWizardWelcomeStep;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class BudgetWizardPanel extends WizardPanel
{
	public BudgetWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions  = mainWindow.getActions();
		
		DEVELOP_BUDGET = addStep(new BudgetWizardDevelopBudget(this));
		addStep(new BudgetWizardAccountingAndFunding(this));
		addStep(new BudgetWizardBudgetDetail(this));
		WIZARD_DEMO = addStep(new BudgetWizardDemo(this));
		
		setStep(DEVELOP_BUDGET);
	}

	public void next() throws Exception
	{
		super.next();
		if (currentStep == WIZARD_DEMO)
			actions.get(ActionJumpScheduleWizardWelcomeStep.class).doAction();
	}

	public void previous() throws Exception
	{
		super.previous();
	}
	
	int DEVELOP_BUDGET;
	int WIZARD_DEMO;
	Actions actions;
}
