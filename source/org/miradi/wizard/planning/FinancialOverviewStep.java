/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.actions.views.ActionViewBudget;
import org.miradi.wizard.FinancialWizardStep;
import org.miradi.wizard.WizardPanel;

public class FinancialOverviewStep extends FinancialWizardStep
{
	public FinancialOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewBudget.class;
	}
}
