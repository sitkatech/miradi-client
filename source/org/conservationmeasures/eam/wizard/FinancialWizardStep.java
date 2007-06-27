/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.views.budget.BudgetView;

public class FinancialWizardStep extends WizardStep
{

	public FinancialWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, BudgetView.getViewName());
	}

}
