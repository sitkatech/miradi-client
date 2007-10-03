/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.FinancialWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class BudgetWizardAccountingAndFunding extends FinancialWizardStep
{
	public BudgetWizardAccountingAndFunding(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_4A;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpBudgetWizardAccountingAndFunding.class;
	}

	public String getSubHeading()
	{
		return EAM.text("Page 1");
	}
}
