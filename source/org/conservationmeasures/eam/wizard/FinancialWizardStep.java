/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.views.planning.PlanningView;

public class FinancialWizardStep extends SplitWizardStep
{

	public FinancialWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, PlanningView.getViewName());
	}

}
