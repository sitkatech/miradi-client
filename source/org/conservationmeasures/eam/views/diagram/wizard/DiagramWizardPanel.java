/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyContributingFactors;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringFocus;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringWizardFocusStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class DiagramWizardPanel extends WizardPanel
{
	public DiagramWizardPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		actions = mainWindow.getActions();
		
		OVERVIEW = addStep(new DiagramWizardOverviewStep(this));
		PROJECT_SCOPE = addStep(new DiagramWizardProjectScopeStep(this));
		VISION = addStep(new DiagramWizardVisionStep(this));
		CONSERVATION_TARGET = addStep(new DiagramWizardDefineTargetsStep(this));
		addStep(new DiagramWizardReviewAndModifyTargetsStep(this));
		addStep(new DescribeTargetStatusStep(this));
		IDENTIFY_DIRECT_THREATS = addStep(new DiagramWizardIdentifyDirectThreatStep(this));
		addStep(new DiagramWizardLinkDirectThreatsToTargetsStep(this));
		IDENTIFY_INDIRECT_THREATS = addStep(new DiagramWizardIdentifyIndirectThreatStep(this));
		addStep(new DiagramWizardConstructChainsStep(this));
		SELECT_CHAIN = addStep(new SelectChainStep(this));
		addStep(new DevelopDraftStrategiesStep(this));
		addStep(new RankDraftStrategiesStep(this));
		addStep(new EditAllStrategiesStep(this));
		REVIEW_AND_ADJUST = addStep(new DiagramWizardReviewModelAndAdjustStep(this));
		DEVELOP_GOAL = addStep(new StrategicPlanDevelopGoalStep(this));
		DEVELOP_OBJECTIVE = addStep(new StrategicPlanDevelopObjectivesStep(this));	
		MONITORING_FOCUS = addStep(new MonitoringWizardFocusStep(this));
		DEFINE_INDICATOR = addStep(new MonitoringWizardDefineIndicatorsStep(this));

		setStep(OVERVIEW);
	}

	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpDefineIndicators.class))
			setStep(DEFINE_INDICATOR);
		else if(stepMarker.equals(ActionJumpMonitoringFocus.class))
			setStep(MONITORING_FOCUS);
		else if(stepMarker.equals(ActionJumpDefineScope.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpEstablishVision.class))
			setStep(VISION);
		else if(stepMarker.equals(ActionJumpIdentifyTargets.class))
			setStep(CONSERVATION_TARGET);
		else if(stepMarker.equals(ActionJumpIdentifyDirectThreats.class))
			setStep(IDENTIFY_DIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpIdentifyContributingFactors.class))
			setStep(IDENTIFY_INDIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpCreateModel.class))
			setStep(OVERVIEW);
		else if (stepMarker.equals(ActionJumpDevelopTargetGoals.class))
			setStep(DEVELOP_GOAL);
		else if (stepMarker.equals(ActionJumpDevelopObjectives.class))
			setStep(DEVELOP_OBJECTIVE);
		else if (stepMarker.equals(ActionJumpIdentifyStrategies.class))
			setStep(SELECT_CHAIN);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if (currentStep == MONITORING_FOCUS)
			actions.get(ActionJumpMonitoringOverview.class).doAction();
		else if (currentStep == DEVELOP_GOAL)
			actions.get(ActionJumpStratPlanWelcome.class).doAction();
		else if (currentStep == DEVELOP_OBJECTIVE)
			actions.get(ActionJumpViewAllGoals.class).doAction();
		else if (currentStep == OVERVIEW)
			 actions.get(ActionJumpSelectTeam.class).doAction();
		 
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
		else if (currentStep == DEFINE_INDICATOR)
			actions.get(ActionJumpEditIndicators.class).doAction();
		
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
	private int DEFINE_INDICATOR;
	private int MONITORING_FOCUS;
	private int SELECT_CHAIN;
}
