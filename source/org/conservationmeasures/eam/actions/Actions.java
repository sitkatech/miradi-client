/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessResources;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessRisks;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetFutureDemo;
import org.conservationmeasures.eam.actions.jump.ActionJumpBudgetWizardAccountingAndFunding;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopFundingProposals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopMap;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopSchedule;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpEditAllStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpFinalizeConceptualModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementStrategicAndMonitoringPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementWorkPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardEditIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardSelectMethodsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpObtainFinancing;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanProjectLifespan;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanningOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanningWizardFinalizeMonitoringPlanStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanningWizardFinalizeStrategicPlanStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpScheduleOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanHowToConstructStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanViewAllObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineProjecScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineProjectLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineProjectVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViability3Step;
import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotalsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.actions.views.ActionViewBudget;
import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.actions.views.ActionViewMap;
import org.conservationmeasures.eam.actions.views.ActionViewMonitoring;
import org.conservationmeasures.eam.actions.views.ActionViewPlanning;
import org.conservationmeasures.eam.actions.views.ActionViewSchedule;
import org.conservationmeasures.eam.actions.views.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.views.ActionViewSummary;
import org.conservationmeasures.eam.actions.views.ActionViewTargetViability;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap();
		
		registerAction(new ActionAbout(mainWindow));
		registerAction(new ActionAboutBenetech(mainWindow));
		registerAction(new ActionAboutCMP(mainWindow));
		registerAction(new ActionHelpComingAttractions(mainWindow));
		registerAction(new ActionHelpAdaptiveManagement(mainWindow));
		registerAction(new ActionHelpAgileSoftware(mainWindow));
		registerAction(new ActionHelpCMPStandards(mainWindow));
		registerAction(new ActionHelpCredits(mainWindow));
		registerAction(new ActionHelpButtonExamples(mainWindow));
		registerAction(new ActionHelpButtonMoreInfo(mainWindow));
		registerAction(new ActionHelpButtonWorkshop(mainWindow));
		registerAction(new ActionHelpButtonSupport(mainWindow));
		registerAction(new ActionClose(mainWindow));
		registerAction(new ActionContextualHelp(mainWindow));
		registerAction(new ActionCopy(mainWindow));
		registerAction(new ActionCut(mainWindow));
		registerAction(new ActionDelete(mainWindow));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionPrint(mainWindow));
		registerAction(new ActionInsertIntermediateResult(mainWindow));
		registerAction(new ActionInsertThreatReductionResult(mainWindow));
		registerAction(new ActionInsertFactorLink(mainWindow));
		registerAction(new ActionInsertTarget(mainWindow));
		registerAction(new ActionInsertDraftStrategy(mainWindow));
		registerAction(new ActionInsertStrategy(mainWindow));
		registerAction(new ActionInsertDirectThreat(mainWindow));
		registerAction(new ActionInsertContributingFactor(mainWindow));
		registerAction(new ActionNewProject(mainWindow));
		registerAction(new ActionCopyProjectTo(mainWindow));
		registerAction(new ActionExportZippedProjectFile(mainWindow));
		registerAction(new ActionExportProjectReportFile(mainWindow));
		registerAction(new ActionExportProjectXml(mainWindow));
		registerAction(new ActionImportZippedProjectFile(mainWindow));
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
		registerAction(new ActionViewPlanning(mainWindow));
		registerAction(new ActionViewBudget(mainWindow));
		registerAction(new ActionViewWorkPlan(mainWindow));
		registerAction(new ActionViewMap(mainWindow));
		registerAction(new ActionViewStrategicPlan(mainWindow));
		registerAction(new ActionViewImages(mainWindow));
		registerAction(new ActionViewSchedule(mainWindow));
		registerAction(new ActionViewMonitoring(mainWindow));
		registerAction(new ActionViewTargetViability(mainWindow));
		
		registerAction(new ActionCreateSlide(mainWindow));
		registerAction(new ActionDeleteSlide(mainWindow));
		registerAction(new ActionToggleSlideShowPanel(mainWindow));
		registerAction(new ActionMoveSlideUp(mainWindow));
		registerAction(new ActionMoveSlideDown(mainWindow));
		registerAction(new ActionSlideShowViewer(mainWindow));
		
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
		registerAction(new ActionTreeCreateActivityIconOnly(mainWindow));
		registerAction(new ActionTreeCreateMethod(mainWindow));
		registerAction(new ActionTreeCreateMethodIconOnly(mainWindow));
		registerAction(new ActionTreeCreateTask(mainWindow));
		registerAction(new ActionTreeCreateTaskIconOnly(mainWindow));
		registerAction(new ActionCreateActivity(mainWindow));
		registerAction(new ActionDeleteActivity(mainWindow));
		registerAction(new ActionCreateResource(mainWindow));
		registerAction(new ActionModifyResource(mainWindow));
		registerAction(new ActionDeleteResource(mainWindow));
		registerAction(new ActionShareActivity(mainWindow));
		registerAction(new ActionShareMethod(mainWindow));
		
		registerAction(new ActionCreateIndicator(mainWindow));
		registerAction(new ActionCloneIndicator(mainWindow));
		registerAction(new ActionDeleteIndicator(mainWindow));
		
		registerAction(new ActionCreateObjective(mainWindow));
		registerAction(new ActionCloneObjective(mainWindow));
		registerAction(new ActionDeleteObjective(mainWindow));
		
		registerAction(new ActionCreateGoal(mainWindow));
		registerAction(new ActionCloneGoal(mainWindow));
		registerAction(new ActionDeleteGoal(mainWindow));
		
		registerAction(new ActionCreateStress(mainWindow));
		registerAction(new ActionDeleteStress(mainWindow));
		registerAction(new ActionCloneStress(mainWindow));
		registerAction(new ActionCreateStressFromKea(mainWindow));
		registerAction(new ActionManageStresses(mainWindow));
		
		registerAction(new ActionCreateKeyEcologicalAttribute(mainWindow));
		registerAction(new ActionDeleteKeyEcologicalAttribute(mainWindow));
		registerAction(new ActionCreateKeyEcologicalAttributeIndicator(mainWindow));
		registerAction(new ActionDeleteKeyEcologicalAttributeIndicator(mainWindow));
		registerAction(new ActionCreateKeyEcologicalAttributeMeasurement(mainWindow));
		registerAction(new ActionDeleteKeyEcologicalAttributeMeasurement(mainWindow));
		registerAction(new ActionDeleteIndicatorMeasurement(mainWindow));
		registerAction(new ActionCreateIndicatorMeasurement(mainWindow));
	
		registerAction(new ActionViewPossibleTeamMembers(mainWindow));
		registerAction(new ActionTeamCreateMember(mainWindow));
		registerAction(new ActionTeamRemoveMember(mainWindow));
		
		registerAction(new ActionResourceListAdd(mainWindow));
		registerAction(new ActionResourceListRemove(mainWindow));
		registerAction(new ActionResourceListModify(mainWindow));
		registerAction(new ActionViewPossibleResources(mainWindow));
		
		registerAction(new ActionAssignResource(mainWindow));
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
		
		registerAction(new ActionJumpSummaryWizardDefineProjectLeader(mainWindow));
		registerAction(new ActionJumpSummaryWizardRolesAndResponsibilities(mainWindow));
		registerAction(new ActionJumpSummaryWizardDefineTeamMembers(mainWindow));
		registerAction(new ActionJumpSummaryWizardDefineProjecScope(mainWindow));
		registerAction(new ActionJumpSummaryWizardDefineProjectVision(mainWindow));
		registerAction(new ActionJumpDiagramWizardDefineTargetsStep(mainWindow));
		registerAction(new ActionJumpTargetViabilityMethodChoiceStep(mainWindow));
		registerAction(new ActionJumpTargetViability3Step(mainWindow));
		registerAction(new ActionJumpDiagramWizardIdentifyDirectThreatStep(mainWindow));
		registerAction(new ActionJumpThreatMatrixOverviewStep(mainWindow));
		registerAction(new ActionJumpDiagramWizardIdentifyIndirectThreatStep(mainWindow));
		registerAction(new ActionJumpAssessStakeholders(mainWindow));
		registerAction(new ActionJumpAnalyzeProjectCapacity(mainWindow));
		registerAction(new ActionJumpArticulateCoreAssumptions(mainWindow));
		registerAction(new ActionJumpDiagramWizardReviewModelAndAdjustStep(mainWindow));
		registerAction(new ActionJumpGroundTruthRevise(mainWindow));
		registerAction(new ActionJumpPlanningOverviewStep(mainWindow));
		registerAction(new ActionJumpStrategicPlanDevelopGoalStep(mainWindow));
		registerAction(new ActionJumpSelectChainStep(mainWindow));
		registerAction(new ActionJumpStrategicPlanDevelopObjectivesStep(mainWindow));
		registerAction(new ActionJumpRankDraftStrategiesStep(mainWindow));
		registerAction(new ActionJumpDiagramWizardResultsChainStep(mainWindow));
		registerAction(new ActionJumpActivitiesAndActionPlan(mainWindow));
		registerAction(new ActionJumpDevelopDraftStrategiesStep(mainWindow));
		registerAction(new ActionJumpAnalyzeResourcesFeasibilityAndRisk(mainWindow));
		registerAction(new ActionJumpMonitoringWizardFocusStep(mainWindow));
		registerAction(new ActionJumpDefineAudiences(mainWindow));
		registerAction(new ActionJumpMonitoringWizardDefineIndicatorsStep(mainWindow));
		registerAction(new ActionJumpSelectAppropriateMethods(mainWindow));
		registerAction(new ActionJumpPlanDataStorage(mainWindow));
		registerAction(new ActionJumpShorttermPlans(mainWindow));
		registerAction(new ActionJumpScheduleOverviewStep(mainWindow));
		registerAction(new ActionJumpDefineTasks(mainWindow));
//		registerAction(new ActionJumpFinancialOverviewStep(mainWindow));
		registerAction(new ActionJumpTeamRoles(mainWindow));
		registerAction(new ActionJumpImplementWorkPlan(mainWindow));
		registerAction(new ActionJumpRefinePlans(mainWindow));
		registerAction(new ActionJumpAnalyzeData(mainWindow));
		registerAction(new ActionJumpAnalyzeStrategies(mainWindow));
		registerAction(new ActionJumpCommunicateResults(mainWindow));
		registerAction(new ActionJumpAdaptAndMonitorPlans(mainWindow));
		registerAction(new ActionJumpDocument(mainWindow));
		registerAction(new ActionJumpShare(mainWindow));
		registerAction(new ActionJumpCreate(mainWindow));
		registerAction(new ActionJumpCloseTheLoop(mainWindow));
		registerAction(new ActionJumpPlanningWizardFinalizeStrategicPlanStep(mainWindow));
		registerAction(new ActionJumpPlanningWizardFinalizeMonitoringPlanStep(mainWindow));
		registerAction(new ActionJumpStrategicPlanViewAllGoals(mainWindow));
		registerAction(new ActionJumpStrategicPlanViewAllObjectives(mainWindow));
		registerAction(new ActionJumpStrategicPlanHowToConstructStep(mainWindow));
		registerAction(new ActionJumpMonitoringWizardEditIndicatorsStep(mainWindow));
//		registerAction(new ActionJumpMonitoringPlanOverviewStep(mainWindow));
		registerAction(new ActionJumpMonitoringWizardSelectMethodsStep(mainWindow));
		registerAction(new ActionJumpWorkPlanAssignResourcesStep(mainWindow));
		registerAction(new ActionJumpWorkPlanDevelopMethodsAndTasksStep(mainWindow));
		registerAction(new ActionJumpWorkPlanDevelopActivitiesAndTasksStep(mainWindow));
		registerAction(new ActionJumpWorkPlanAssignResourcesStep(mainWindow));
		registerAction(new ActionJumpDiagramWizardReviewModelAndAdjustStep(mainWindow));
		registerAction(new ActionJumpThreatRatingWizardCheckTotalsStep(mainWindow));
		registerAction(new ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep(mainWindow));
		registerAction(new ActionJumpEditAllStrategiesStep(mainWindow));
		registerAction(new ActionJumpWorkPlanDevelopActivitiesAndTasksStep(mainWindow));
		registerAction(new ActionJumpWorkPlanDevelopMethodsAndTasksStep(mainWindow));
		registerAction(new ActionJumpDevelopMap(mainWindow));
		registerAction(new ActionJumpDiagramWizardConstructChainsStep(mainWindow));
		registerAction(new ActionJumpFinalizeConceptualModel(mainWindow));
		registerAction(new ActionJumpAssessResources(mainWindow));
		registerAction(new ActionJumpAssessRisks(mainWindow));
		registerAction(new ActionJumpPlanProjectLifespan(mainWindow));
		registerAction(new ActionJumpDevelopSchedule(mainWindow));
		registerAction(new ActionJumpBudgetWizardAccountingAndFunding(mainWindow));
		registerAction(new ActionJumpDevelopFundingProposals(mainWindow));
		registerAction(new ActionJumpObtainFinancing(mainWindow));
		registerAction(new ActionJumpImplementStrategicAndMonitoringPlans(mainWindow));
		
		registerAction(new ActionJumpScheduleOverviewStep(mainWindow));
//		registerAction(new ActionJumpFinancialOverviewStep(mainWindow));
		registerAction(new ActionJumpBudgetFutureDemo(mainWindow));
		registerAction(new ActionConfigureExport(mainWindow));
		registerAction(new ActionDatabasesDemo(mainWindow));
		registerAction(new ActionReportsDemo(mainWindow));
		registerAction(new ActionJumpDiagramOverviewStep(mainWindow));
//		registerAction(new ActionJumpWorkPlanOverviewStep(mainWindow));
		registerAction(new ActionExportBudgetTableTree(mainWindow));
		registerAction(new ActionCreateBendPoint(mainWindow));
		registerAction(new ActionDeleteBendPoint(mainWindow));
		registerAction(new ActionRenameResultsChain(mainWindow));
		registerAction(new ActionRenameConceptualModel(mainWindow));
		registerAction(new ActionWizardNext(mainWindow));
		registerAction(new ActionWizardPrevious(mainWindow));
		registerAction(new ActionShowCellRatings(mainWindow));
		registerAction(new ActionHideCellRatings(mainWindow));
		registerAction(new ActionCreateResultsChain(mainWindow));
		registerAction(new ActionShowResultsChain(mainWindow));
		registerAction(new ActionDeleteResultsChain(mainWindow));
		registerAction(new ActionShowConceptualModel(mainWindow));
		registerAction(new ActionCreateOrShowResultsChain(mainWindow));
		registerAction(new ActionInsertTextBox(mainWindow));
		registerAction(new ActionCreateConceptualModel(mainWindow));
		registerAction(new ActionDeleteConceptualModel(mainWindow));
		registerAction(new ActionCreatePlanningViewConfiguration(mainWindow));
		registerAction(new ActionDeletePlanningViewConfiguration(mainWindow));
		registerAction(new ActionRenamePlanningViewConfiguration(mainWindow));
		registerAction(new ActionDeletePlanningViewTreeNode(mainWindow));
	}
	
	public EAMAction get(Class c)
	{
		Object action = actions.get(c);
		if(action == null)
			throw new RuntimeException("Unknown action: " + c);
		
		return (EAMAction)action;
	}
	
	public MainWindowAction getMainWindowAction(Class c)
	{
		return (MainWindowAction)get(c);
	}
	
	public ObjectsAction getObjectsAction(Class c)
	{
		return (ObjectsAction)get(c);
	}

	public void updateActionStates()
	{
		boolean verbose = false;
		if(verbose)
		{
			try
			{
				throw new Exception();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		EAM.logVerbose("updateActionStates");
		HashMap<EAMAction,Boolean> newStates = new HashMap<EAMAction, Boolean>();
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			EAMAction action = (EAMAction)iter.next();
			boolean shouldBeEnabled = action.shouldBeEnabled();
			newStates.put(action, shouldBeEnabled);
		}
		SwingUtilities.invokeLater(new DelayedActionSetEnabled(newStates));
	}
	
	class DelayedActionSetEnabled implements Runnable
	{
		public DelayedActionSetEnabled(HashMap<EAMAction,Boolean> newStatesToApply)
		{
			newStates = newStatesToApply;
		}

		public void run()
		{
			for(EAMAction action : newStates.keySet())
			{
				action.setEnabled(newStates.get(action));
			}
		}
		
		HashMap<EAMAction,Boolean> newStates;
	}
	
	void registerAction(EAMAction action)
	{
		actions.put(action.getClass(), action);
	}

	Map actions;
}
