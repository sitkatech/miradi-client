/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringFocus;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class MonitoringPlanWizardPanel extends WizardPanel
{
	public MonitoringPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions = mainWindow.getActions();
		
		OVERVIEW = addStep(new MonitoringWizardOverviewStep(this));
		EDIT_INDICATORS = addStep(new MonitoringWizardEditIndicatorsStep(this));
		setStep(OVERVIEW);
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpMonitoringOverview.class))
			setStep(OVERVIEW);
		else if(stepMarker.equals(ActionJumpEditIndicators.class))
			setStep(EDIT_INDICATORS);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if(currentStep == EDIT_INDICATORS)
			actions.get(ActionJumpDefineIndicators.class).doAction();
		else if (currentStep == OVERVIEW)
			actions.get(ActionJumpViewAllObjectives.class).doAction();
		
		super.previous();
	}
	
	public void next() throws Exception
	{
		if(currentStep == OVERVIEW)
			actions.get(ActionJumpMonitoringFocus.class).doAction();
		else if(currentStep == EDIT_INDICATORS)
			actions.get(ActionJumpSelectMethod.class).doAction();
		
		super.next();
	}
	
	Actions actions;

	int OVERVIEW;
	int EDIT_INDICATORS;
	int SELECT_METHODS;
}
