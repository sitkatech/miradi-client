/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanHowToConstructStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllObjectives;
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
		if (stepMarker.equals(ActionJumpStrategicPlanViewAllGoals.class))
			setStep(VIEW_ALL_GOALS);
		else if (stepMarker.equals(ActionJumpStrategicPlanViewAllObjectives.class))
			setStep(VIEW_ALL_OBJECTIVES);
		else if (stepMarker.equals(ActionJumpStrategicPlanHowToConstructStep.class))
			setStep(HOW_TO_CONSTRUCT_STRAT_PLAN);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if (currentStep == VIEW_ALL_GOALS)
			actions.get(ActionJumpStrategicPlanDevelopGoalStep.class).doAction();
		if (currentStep == VIEW_ALL_OBJECTIVES)
			actions.get(ActionJumpStrategicPlanDevelopObjectivesStep.class).doAction();
		if (currentStep == HOW_TO_CONSTRUCT_STRAT_PLAN)
			actions.get(ActionJumpDiagramWizardReviewModelAndAdjustStep.class).doAction();
		
		super.previous();
	}
	
	public void next() throws Exception
	{
		if (currentStep == HOW_TO_CONSTRUCT_STRAT_PLAN)
			actions.get(ActionJumpStrategicPlanDevelopGoalStep.class).doAction();
		if (currentStep == VIEW_ALL_GOALS)
			actions.get(ActionJumpStrategicPlanDevelopObjectivesStep.class).doAction();
		if (currentStep == VIEW_ALL_OBJECTIVES)
			actions.get(ActionJumpSelectChainStep.class).doAction();
		
		super.next();
	}
	
	Actions actions;
	int VIEW_ALL_GOALS;
	int VIEW_ALL_OBJECTIVES;
	int HOW_TO_CONSTRUCT_STRAT_PLAN;
}
