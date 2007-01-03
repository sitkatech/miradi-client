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
		DEFINE_INDICATORS = addStep(new MonitoringDefineIndicatorsStep(this));
		EDIT_NDICATORS = addStep(new MonitoringEditIndicatorsStep(this));
		SELECT_METHODS = addStep(new MonitoringSelectMethodsStep(this));
		setStep(OVERVIEW);
	}
	
	int OVERVIEW;
	int FOCUS;
	int DEFINE_INDICATORS;
	int EDIT_NDICATORS;
	int SELECT_METHODS;
}
