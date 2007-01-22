/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetFutureDemo;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopSchedule;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
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

	public void jump(Class stepMarker) throws Exception
	{
		if (stepMarker.equals(ActionJumpDevelopBudgets.class))
			setStep(DEVELOP_BUDGET);
		else if(stepMarker.equals(ActionJumpBudgetFutureDemo.class))
			setStep(WIZARD_DEMO);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	public void next() throws Exception
	{
		if (currentStep == WIZARD_DEMO)
			actions.get(ActionJumpDevelopSchedule.class).doAction();
		else
			super.next();

	}

	public void previous() throws Exception
	{
		if (currentStep == DEVELOP_BUDGET)
			actions.get(ActionJumpWorkPlanAssignResourcesStep.class).doAction();
		else
			super.previous();

	}
	
	int DEVELOP_BUDGET;
	int WIZARD_DEMO;
	Actions actions;
}
