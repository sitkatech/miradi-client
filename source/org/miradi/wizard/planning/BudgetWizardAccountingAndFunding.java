/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.FinancialWizardStep;
import org.miradi.wizard.WizardPanel;

public class BudgetWizardAccountingAndFunding extends FinancialWizardStep
{
	public BudgetWizardAccountingAndFunding(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpBudgetWizardAccountingAndFunding.class;
	}

	public String getSubHeading()
	{
		return EAM.text("1) List accounting codes and funding sources");
	}
}
