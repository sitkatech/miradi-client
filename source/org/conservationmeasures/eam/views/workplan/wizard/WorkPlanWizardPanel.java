/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardSelectMethodsStep;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class WorkPlanWizardPanel extends WizardPanel
{
	public WorkPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions = mainWindow.getActions();
		
		WELCOME = addStep(new WorkPlanOverviewStep(this));
		SELECT_METHOD = addStep(new MonitoringWizardSelectMethodsStep(this));
		addStep(new WorkPlanDevelopActivitiesAndTasksStep(this));
		addStep(new WorkPlanDevelopMethodsAndTasksStep(this));
		addStep(new WorkPlanCreateResourcesStep(this));
		addStep(new WorkPlanAssignResourcesStep(this));
		setStep(WELCOME);
	}
	
	
	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpSelectMethod.class))
			setStep(SELECT_METHOD);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if(currentStep == SELECT_METHOD)
			actions.get(ActionJumpEditIndicators.class).doAction();
		
		super.previous();
	}
	
	Actions actions;
	
	int WELCOME;
	int SELECT_METHOD;
}
