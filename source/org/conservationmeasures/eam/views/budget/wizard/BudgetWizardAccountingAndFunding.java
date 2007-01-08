/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class BudgetWizardAccountingAndFunding extends WizardStep
{
	public BudgetWizardAccountingAndFunding(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "AccountingCodesAndFundingSources.html";

}
