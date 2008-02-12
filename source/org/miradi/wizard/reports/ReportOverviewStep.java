/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.reports;

import org.miradi.actions.views.ActionViewReport;
import org.miradi.views.reports.ReportView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

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
