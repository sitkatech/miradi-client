/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyIndirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class DiagramWizardPanel extends WizardPanel
{
	public DiagramWizardPanel(Actions actionsToUse) throws Exception
	{
		actions = actionsToUse;
		
		
		OVERVIEW = addStep(new DiagramWizardOverviewStep(this));
		PROJECT_SCOPE = addStep(new DiagramWizardProjectScopeStep(this));
		VISION = addStep(new DiagramWizardVisionStep(this));
		CONSERVATION_TARGET = addStep(new DiagramWizardDefineTargetsStep(this));
		addStep(new DiagramWizardReviewAndModifyTargetsStep(this));
		IDENTIFY_DIRECT_THREATS = addStep(new DiagramWizardIdentifyDirectThreatStep(this));
		addStep(new DiagramWizardLinkDirectThreatsToTargetsStep(this));
		IDENTIFY_INDIRECT_THREATS = addStep(new DiagramWizardIdentifyIndirectThreatStep(this));
		addStep(new DiagramWizardConstructChainsStep(this));
		REVIEW_AND_ADJUST = addStep(new DiagramWizardReviewModelAndAdjustStep(this));
		
		setStep(OVERVIEW);
	}

	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpDefineScope.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpEstablishVision.class))
			setStep(VISION);
		else if(stepMarker.equals(ActionJumpIdentifyTargets.class))
			setStep(CONSERVATION_TARGET);
		else if(stepMarker.equals(ActionJumpIdentifyDirectThreats.class))
			setStep(IDENTIFY_DIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpIdentifyIndirectThreats.class))
			setStep(IDENTIFY_INDIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpCreateModel.class))
			setStep(OVERVIEW);
		else if (stepMarker.equals(ActionJumpDevelopTargetGoals.class))
		{
			DEVELOP_GOAL = addStep(new StrategicPlanDevelopGoalStep(this));
			setStep(DEVELOP_GOAL);
		}
		else if (stepMarker.equals(ActionJumpDevelopObjectives.class))
		{
			DEVELOP_OBJECTIVE = addStep(new StrategicPlanDevelopObjectivesStep(this));
			setStep(DEVELOP_OBJECTIVE);
		}
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if (currentStep == DEVELOP_GOAL)
			actions.get(ActionJumpStratPlanWelcome.class).doAction();
		 if (currentStep == DEVELOP_OBJECTIVE)
			actions.get(ActionJumpViewAllGoals.class).doAction();
		 
		super.previous();
	}
	
	public void next() throws Exception
	{
		if(currentStep == REVIEW_AND_ADJUST)
			actions.get(ActionJumpRankDirectThreats.class).doAction();
		else if (currentStep == DEVELOP_GOAL)
			actions.get(ActionJumpViewAllGoals.class).doAction();
		else if (currentStep == DEVELOP_OBJECTIVE)
			actions.get(ActionJumpViewAllObjectives.class).doAction();
		
		super.next();
	}

	Actions actions;

	private int DEVELOP_GOAL = -1;
	private int DEVELOP_OBJECTIVE = -1;
	
	private int OVERVIEW;
	private int PROJECT_SCOPE;
	private int VISION;
	private int CONSERVATION_TARGET;
	private int IDENTIFY_DIRECT_THREATS;
	private int IDENTIFY_INDIRECT_THREATS;
	private int REVIEW_AND_ADJUST;
}
