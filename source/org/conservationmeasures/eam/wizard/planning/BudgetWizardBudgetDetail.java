/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.planning;

import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.FinancialWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class BudgetWizardBudgetDetail extends FinancialWizardStep
{
	public BudgetWizardBudgetDetail(WizardPanel wizardToUse)
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
		return EAM.text("Page 2");
	}
}
