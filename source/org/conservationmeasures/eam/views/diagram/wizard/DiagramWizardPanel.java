/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyContributingFactors;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpLinkDirectThreatsToTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewModelAndAdjust;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotals;
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
		TARGET_STATUS = addStep(new DescribeTargetStatusStep(this));
		IDENTIFY_DIRECT_THREATS = addStep(new DiagramWizardIdentifyDirectThreatStep(this));
		LINK_DIRECT_THREATS = addStep(new DiagramWizardLinkDirectThreatsToTargetsStep(this));
		IDENTIFY_INDIRECT_THREATS = addStep(new DiagramWizardIdentifyIndirectThreatStep(this));
		addStep(new DiagramWizardConstructChainsStep(this));
		REVIEW_AND_ADJUST = addStep(new DiagramWizardReviewModelAndAdjustStep(this));
		SELECT_CHAIN = addStep(new SelectChainStep(this));
		addStep(new DevelopDraftStrategiesStep(this));
		RANK_DRAFT_STRATEGIES = addStep(new RankDraftStrategiesStep(this));
		EDIT_ALL_STRATEGIES = addStep(new EditAllStrategiesStep(this));
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
		else if(stepMarker.equals(ActionJumpDetermineNeeds.class))
			setStep(MONITORING_FOCUS);
		else if(stepMarker.equals(ActionJumpDefineScope.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpEstablishVision.class))
			setStep(VISION);
		else if(stepMarker.equals(ActionJumpIdentifyTargets.class))
			setStep(CONSERVATION_TARGET);
		else if(stepMarker.equals(ActionJumpDescribeTargets.class))
			setStep(TARGET_STATUS);
		else if(stepMarker.equals(ActionJumpIdentifyDirectThreats.class))
			setStep(IDENTIFY_DIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpIdentifyContributingFactors.class))
			setStep(IDENTIFY_INDIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpLinkDirectThreatsToTargets.class))
			setStep(LINK_DIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpCreateModel.class))
			setStep(REVIEW_AND_ADJUST);
		else if(stepMarker.equals(ActionJumpRankDraftStrategies.class))
			setStep(RANK_DRAFT_STRATEGIES);
		else if (stepMarker.equals(ActionJumpEditAllStrategies.class))
			setStep(EDIT_ALL_STRATEGIES);
		else if (stepMarker.equals(ActionJumpDevelopTargetGoals.class))
			setStep(DEVELOP_GOAL);
		else if (stepMarker.equals(ActionJumpDevelopObjectives.class))
			setStep(DEVELOP_OBJECTIVE);
		else if (stepMarker.equals(ActionJumpIdentifyStrategies.class))
			setStep(SELECT_CHAIN);
		else if (stepMarker.equals(ActionJumpReviewModelAndAdjust.class))
			setStep(REVIEW_AND_ADJUST);
		else if (stepMarker.equals(ActionJumpDiagramOverview.class))
			setStep(OVERVIEW);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void previous() throws Exception
	{
		if (currentStep == MONITORING_FOCUS)
			actions.get(ActionJumpMonitoringOverview.class).doAction();
		else if(currentStep == IDENTIFY_INDIRECT_THREATS)
			actions.get(ActionJumpThreatRatingWizardCheckTotals.class).doAction();
		else if (currentStep == DEVELOP_GOAL)
			actions.get(ActionJumpStratPlanWelcome.class).doAction();
		else if (currentStep == DEVELOP_OBJECTIVE)
			actions.get(ActionJumpViewAllGoals.class).doAction();
		else if(currentStep == SELECT_CHAIN)
			actions.get(ActionJumpViewAllObjectives.class).doAction();
		else if (currentStep == OVERVIEW)
			 actions.get(ActionJumpEstablishVision.class).doAction();
		 
		super.previous();
	}
	
	public void next() throws Exception
	{
		if(currentStep ==  LINK_DIRECT_THREATS)
			actions.get(ActionJumpRankDirectThreats.class).doAction();
		else if(currentStep == EDIT_ALL_STRATEGIES)
			actions.get(ActionJumpMonitoringOverview.class).doAction();
		else if(currentStep == REVIEW_AND_ADJUST)
			actions.get(ActionJumpStratPlanWelcome.class).doAction();
		else if (currentStep == DEVELOP_GOAL)
			actions.get(ActionJumpViewAllGoals.class).doAction();
		else if (currentStep == DEVELOP_OBJECTIVE)
			actions.get(ActionJumpViewAllObjectives.class).doAction();
		else if (currentStep == DEFINE_INDICATOR)
			actions.get(ActionJumpEditIndicators.class).doAction();
		
		super.next();
	}

	Actions actions;

	private int OVERVIEW;
	private int PROJECT_SCOPE;
	private int VISION;
	private int CONSERVATION_TARGET;
	private int TARGET_STATUS;
	private int IDENTIFY_DIRECT_THREATS;
	private int LINK_DIRECT_THREATS;
	private int IDENTIFY_INDIRECT_THREATS;
	private int SELECT_CHAIN;
	private int RANK_DRAFT_STRATEGIES;
	private int EDIT_ALL_STRATEGIES;
	private int REVIEW_AND_ADJUST;

	private int DEVELOP_GOAL;
	private int DEVELOP_OBJECTIVE;
	private int MONITORING_FOCUS;
	private int DEFINE_INDICATOR;
}
