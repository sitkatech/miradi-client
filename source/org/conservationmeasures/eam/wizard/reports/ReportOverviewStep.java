/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.reports;

import org.conservationmeasures.eam.actions.views.ActionViewReport;
import org.conservationmeasures.eam.views.reports.ReportView;
import org.conservationmeasures.eam.wizard.SplitWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class ReportOverviewStep extends SplitWizardStep
{
	public ReportOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, ReportView.getViewName());
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewReport.class;
	}
}
