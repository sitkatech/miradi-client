/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewModelAndAdjust;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class StrategicPlanWizardPanel extends WizardPanel
{
	public StrategicPlanWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions  = mainWindow.getActions();
		
		HOW_TO_CONSTRUCT_STRAT_PLAN = addStep(new StrategicPlanHowToConstructStep(this));
		VIEW_ALL_GOALS = addStep(new StrategicPlanViewAllGoals(this));
		VIEW_ALL_OBJECTIVES = addStep(new StrategicPlanViewAllObjectives(this));
		setStep(HOW_TO_CONSTRUCT_STRAT_PLAN );
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		if (stepMarker.equals(ActionJumpViewAllGoals.class))
			setStep(VIEW_ALL_GOALS);
		else if (stepMarker.equals(ActionJumpViewAllObjectives.class))
			setStep(VIEW_ALL_OBJECTIVES);
		else if (stepMarker.equals(ActionJumpStratPlanWelcome.class))
			setStep(HOW_TO_CONSTRUCT_STRAT_PLAN);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if (currentStep == VIEW_ALL_GOALS)
			actions.get(ActionJumpDevelopTargetGoals.class).doAction();
		if (currentStep == VIEW_ALL_OBJECTIVES)
			actions.get(ActionJumpDevelopObjectives.class).doAction();
		if (currentStep == HOW_TO_CONSTRUCT_STRAT_PLAN)
			actions.get(ActionJumpReviewModelAndAdjust.class).doAction();
		
		super.previous();
	}
	
	public void next() throws Exception
	{
		if (currentStep == HOW_TO_CONSTRUCT_STRAT_PLAN)
			actions.get(ActionJumpDevelopTargetGoals.class).doAction();
		if (currentStep == VIEW_ALL_GOALS)
			actions.get(ActionJumpDevelopObjectives.class).doAction();
		if (currentStep == VIEW_ALL_OBJECTIVES)
			actions.get(ActionJumpIdentifyStrategies.class).doAction();
		
		super.next();
	}
	
	Actions actions;
	int VIEW_ALL_GOALS;
	int VIEW_ALL_OBJECTIVES;
	int HOW_TO_CONSTRUCT_STRAT_PLAN;
}
