/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.reports;

import org.miradi.actions.views.ActionViewReports;
import org.miradi.views.reports.ReportsView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

public class ReportsOverviewStep extends SplitWizardStep
{
	public ReportsOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, ReportsView.getViewName());
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewReports.class;
	}
}
