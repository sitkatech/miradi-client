/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard;

import org.miradi.views.planning.PlanningView;

public class MonitoringPlanWizardStep extends SplitWizardStep
{

	public MonitoringPlanWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, PlanningView.getViewName());
	}

}
