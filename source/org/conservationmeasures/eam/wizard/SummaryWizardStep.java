/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.views.summary.SummaryView;

public class SummaryWizardStep extends SplitWizardStep
{
	public SummaryWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, SummaryView.getViewName());
	}

}
