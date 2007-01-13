/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssignResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivitiesAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopMonitoringMethodsAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
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
		DEVELOP_ACTIVITIES = addStep(new WorkPlanDevelopActivitiesAndTasksStep(this));
		DEVELOP_METHODS = addStep(new WorkPlanDevelopMethodsAndTasksStep(this));
		CREATE_RESOURCES = addStep(new WorkPlanCreateResourcesStep(this));
		ASSIGN_RESOURCES = addStep(new WorkPlanAssignResourcesStep(this));
		setStep(WELCOME);
	}
	
	
	public void jump(Class stepMarker) throws Exception
	{
		if (stepMarker.equals(ActionJumpSelectMethod.class))
			setStep(SELECT_METHOD);
		else if (stepMarker.equals(ActionJumpDevelopActivitiesAndTasks.class))
			setStep(DEVELOP_ACTIVITIES);
		else if (stepMarker.equals(ActionJumpDevelopMonitoringMethodsAndTasks.class))
			setStep(DEVELOP_METHODS);
		else if (stepMarker.equals(ActionJumpAssignResources.class))
			setStep(CREATE_RESOURCES);
		else if (stepMarker.equals(ActionJumpWorkPlanAssignResourcesStep.class))
			setStep(ASSIGN_RESOURCES);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void next() throws Exception
	{
		if (currentStep == ASSIGN_RESOURCES)
			actions.get(ActionJumpDevelopBudgets.class).doAction();
		
		super.next();
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
	int DEVELOP_ACTIVITIES;
	int DEVELOP_METHODS;
	int CREATE_RESOURCES;
	int ASSIGN_RESOURCES;
}
