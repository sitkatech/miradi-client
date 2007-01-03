/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class MonitoringPlanWizardPanel extends WizardPanel
{
	public MonitoringPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		OVERVIEW = addStep(new MonitoringOverviewStep(this));
		FOCUS = addStep(new MonitoringFocusStep(this));
		setStep(OVERVIEW);
	}
	
	int OVERVIEW;
	int FOCUS;
}
