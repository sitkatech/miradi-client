/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.summary;

import org.conservationmeasures.eam.actions.views.ActionViewSummary;
import org.conservationmeasures.eam.wizard.SummaryWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class SummaryOverviewStep extends SummaryWizardStep
{

	public SummaryOverviewStep(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewSummary.class;
	}
	
}

