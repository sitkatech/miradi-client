/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssignResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivities;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivitiesAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudget;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopMonitoringMethodsAndTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyContributingFactors;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpLinkDirectThreatsToTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringFocus;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpResultsChains;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewModelAndAdjust;
import org.conservationmeasures.eam.actions.jump.ActionJumpScheduleWizardWelcomeStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectMethod;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.actions.views.ActionViewBudget;
import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.actions.views.ActionViewMap;
import org.conservationmeasures.eam.actions.views.ActionViewMonitoring;
import org.conservationmeasures.eam.actions.views.ActionViewSchedule;
import org.conservationmeasures.eam.actions.views.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.views.ActionViewSummary;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.diagram.wizard.ActionJumpEditAllStrategies;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap();
		
		registerAction(new ActionAbout(mainWindow));
		registerAction(new ActionHelpComingAttractions(mainWindow));
		registerAction(new ActionHelpAdaptiveManagement(mainWindow));
		registerAction(new ActionHelpAgileSoftware(mainWindow));
		registerAction(new ActionHelpCMPStandards(mainWindow));
		registerAction(new ActionHelpCredits(mainWindow));
		registerAction(new ActionHelpButtonExamples(mainWindow));
		registerAction(new ActionHelpButtonMoreInfo(mainWindow));
		registerAction(new ActionHelpButtonWorkshop(mainWindow));
		registerAction(new ActionClose(mainWindow));
		registerAction(new ActionContextualHelp(mainWindow));
		registerAction(new ActionCopy(mainWindow));
		registerAction(new ActionCut(mainWindow));
		registerAction(new ActionDelete(mainWindow));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionPrint(mainWindow));
		registerAction(new ActionInsertFactorLink(mainWindow));
		registerAction(new ActionInsertTarget(mainWindow));
		registerAction(new ActionInsertDraftStrategy(mainWindow));
		registerAction(new ActionInsertStrategy(mainWindow));
		registerAction(new ActionInsertDirectThreat(mainWindow));
		registerAction(new ActionInsertContributingFactor(mainWindow));
		registerAction(new ActionNewProject(mainWindow));
		registerAction(new ActionProjectSaveAs(mainWindow));
		registerAction(new ActionExportZippedProjectFile(mainWindow));
		registerAction(new ActionImportZippedProjectFile(mainWindow));
		registerAction(new ActionImportTncCapWorkbook(mainWindow));
		registerAction(new ActionProperties(mainWindow));
		registerAction(new ActionSaveImage(mainWindow));
		registerAction(new ActionPaste(mainWindow));
		registerAction(new ActionPasteWithoutLinks(mainWindow));
		registerAction(new ActionRedo(mainWindow));
		registerAction(new ActionSelectAll(mainWindow));
		registerAction(new ActionSelectChain(mainWindow));
		registerAction(new ActionUndo(mainWindow));
		
		registerAction(new ActionViewSummary(mainWindow));
		registerAction(new ActionViewDiagram(mainWindow));
		registerAction(new ActionViewThreatMatrix(mainWindow));
		registerAction(new ActionViewBudget(mainWindow));
		registerAction(new ActionViewWorkPlan(mainWindow));
		registerAction(new ActionViewMap(mainWindow));
		registerAction(new ActionViewStrategicPlan(mainWindow));
		registerAction(new ActionViewImages(mainWindow));
		registerAction(new ActionViewSchedule(mainWindow));
		registerAction(new ActionViewMonitoring(mainWindow));
		
		registerAction(new ActionConfigureLayers(mainWindow));
		registerAction(new ActionShowSelectedChainMode(mainWindow));
		registerAction(new ActionShowFullModelMode(mainWindow));
		registerAction(new ActionZoomIn(mainWindow));
		registerAction(new ActionZoomOut(mainWindow));
		registerAction(new ActionNudgeUp(mainWindow));
		registerAction(new ActionNudgeDown(mainWindow));
		registerAction(new ActionNudgeLeft(mainWindow));
		registerAction(new ActionNudgeRight(mainWindow));
		registerAction(new ActionTreeCreateActivity(mainWindow));
		registerAction(new ActionTreeCreateMethod(mainWindow));
		registerAction(new ActionTreeCreateTask(mainWindow));
		registerAction(new ActionCreateActivity(mainWindow));
		registerAction(new ActionDeleteActivity(mainWindow));
		registerAction(new ActionCreateResource(mainWindow));
		registerAction(new ActionModifyResource(mainWindow));
		registerAction(new ActionDeleteResource(mainWindow));
		registerAction(new ActionCreateIndicator(mainWindow));
		registerAction(new ActionDeleteIndicator(mainWindow));
		registerAction(new ActionCreateObjective(mainWindow));
		registerAction(new ActionDeleteObjective(mainWindow));
		registerAction(new ActionCreateGoal(mainWindow));
		registerAction(new ActionDeleteGoal(mainWindow));
		registerAction(new ActionViewPossibleTeamMembers(mainWindow));
		registerAction(new ActionTeamCreateMember(mainWindow));
		registerAction(new ActionTeamRemoveMember(mainWindow));
		
		registerAction(new ActionResourceListAdd(mainWindow));
		registerAction(new ActionResourceListRemove(mainWindow));
		registerAction(new ActionResourceListModify(mainWindow));
		registerAction(new ActionViewPossibleResources(mainWindow));
		
		registerAction(new ActionAddAssignment(mainWindow));
		registerAction(new ActionRemoveAssignment(mainWindow));
		registerAction(new ActionCreateAccountingCode(mainWindow));
		registerAction(new ActionDeleteAccountingCode(mainWindow));
		registerAction(new ActionImportAccountingCodes(mainWindow));
		registerAction(new ActionCreateFundingSource(mainWindow));
		registerAction(new ActionDeleteFundingSource(mainWindow));
		
		registerAction(new ActionDeleteWorkPlanNode(mainWindow));
		registerAction(new ActionPreferences(mainWindow));
		registerAction(new ActionTreeNodeUp(mainWindow));
		registerAction(new ActionTreeNodeDown(mainWindow));
		
		registerAction(new ActionJumpDesignateLeader(mainWindow));
		registerAction(new ActionJumpDevelopCharter(mainWindow));
		registerAction(new ActionJumpSelectTeam(mainWindow));
		registerAction(new ActionJumpDefineScope(mainWindow));
		registerAction(new ActionJumpEstablishVision(mainWindow));
		registerAction(new ActionJumpIdentifyTargets(mainWindow));
		registerAction(new ActionJumpDescribeTargets(mainWindow));
		registerAction(new ActionJumpIdentifyDirectThreats(mainWindow));
		registerAction(new ActionJumpRankDirectThreats(mainWindow));
		registerAction(new ActionJumpIdentifyContributingFactors(mainWindow));
		registerAction(new ActionJumpAssessStakeholders(mainWindow));
		registerAction(new ActionJumpAnalyzeProjectCapacity(mainWindow));
		registerAction(new ActionJumpArticulateCoreAssumptions(mainWindow));
		registerAction(new ActionJumpCreateModel(mainWindow));
		registerAction(new ActionJumpGroundTruthRevise(mainWindow));
		registerAction(new ActionJumpDevelopTargetGoals(mainWindow));
		registerAction(new ActionJumpIdentifyStrategies(mainWindow));
		registerAction(new ActionJumpDevelopObjectives(mainWindow));
		registerAction(new ActionJumpRankDraftStrategies(mainWindow));
		registerAction(new ActionJumpResultsChains(mainWindow));
		registerAction(new ActionJumpActivitiesAndActionPlan(mainWindow));
		registerAction(new ActionJumpAnalyzeResourcesFeasibilityAndRisk(mainWindow));
		registerAction(new ActionJumpDetermineNeeds(mainWindow));
		registerAction(new ActionJumpDefineAudiences(mainWindow));
		registerAction(new ActionJumpDefineIndicators(mainWindow));
		registerAction(new ActionJumpSelectAppropriateMethods(mainWindow));
		registerAction(new ActionJumpPlanDataStorage(mainWindow));
		registerAction(new ActionJumpShorttermPlans(mainWindow));
		registerAction(new ActionJumpDevelopActivities(mainWindow));
		registerAction(new ActionJumpDefineTasks(mainWindow));
		registerAction(new ActionJumpDevelopBudgets(mainWindow));
		registerAction(new ActionJumpTeamRoles(mainWindow));
		registerAction(new ActionJumpImplementPlans(mainWindow));
		registerAction(new ActionJumpRefinePlans(mainWindow));
		registerAction(new ActionJumpAnalyzeData(mainWindow));
		registerAction(new ActionJumpAnalyzeStrategies(mainWindow));
		registerAction(new ActionJumpCommunicateResults(mainWindow));
		registerAction(new ActionJumpAdaptAndMonitorPlans(mainWindow));
		registerAction(new ActionJumpDocument(mainWindow));
		registerAction(new ActionJumpShare(mainWindow));
		registerAction(new ActionJumpCreate(mainWindow));
		registerAction(new ActionJumpCloseTheLoop(mainWindow));
		registerAction(new ActionJumpViewAllGoals(mainWindow));
		registerAction(new ActionJumpViewAllObjectives(mainWindow));
		registerAction(new ActionJumpStratPlanWelcome(mainWindow));
		registerAction(new ActionJumpMonitoringFocus(mainWindow));
		registerAction(new ActionJumpEditIndicators(mainWindow));
		registerAction(new ActionJumpMonitoringOverview(mainWindow));
		registerAction(new ActionJumpSelectMethod(mainWindow));
		registerAction(new ActionJumpAssignResources(mainWindow));
		registerAction(new ActionJumpDevelopMonitoringMethodsAndTasks(mainWindow));
		registerAction(new ActionJumpDevelopActivitiesAndTasks(mainWindow));
		registerAction(new ActionJumpScheduleWizardWelcomeStep(mainWindow));
		registerAction(new ActionJumpWorkPlanAssignResourcesStep(mainWindow));
		registerAction(new ActionJumpDevelopBudget(mainWindow));
		registerAction(new ActionJumpReviewModelAndAdjust(mainWindow));
		registerAction(new ActionJumpThreatRatingWizardCheckTotals(mainWindow));
		registerAction(new ActionJumpLinkDirectThreatsToTargets(mainWindow));
		registerAction(new ActionJumpEditAllStrategies(mainWindow));
	}
	
	public EAMAction get(Class c)
	{
		Object action = actions.get(c);
		if(action == null)
			throw new RuntimeException("Unknown action: " + c);
		
		return (EAMAction)action;
	}
	
	public ObjectsAction getObjectsAction(Class c)
	{
		return (ObjectsAction)get(c);
	}

	public void updateActionStates()
	{
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			EAMAction action = (EAMAction)iter.next();
			action.updateEnabledState();
		}
	}
	
	void registerAction(EAMAction action)
	{
		actions.put(action.getClass(), action);
	}

	Map actions;
}
