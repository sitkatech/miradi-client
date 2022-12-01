/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.actions;

import org.miradi.actions.jump.*;
import org.miradi.actions.openstandards.*;
import org.miradi.actions.views.*;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Actions
{
	public Actions(MainWindow mainWindow)
	{
		actions = new HashMap<Class, MiradiAction>();
		codeToJumpMenuActionMap = new HashMap<String, AbstractJumpMenuAction>();
		
		registerAction(new ActionAbout(mainWindow));
		registerAction(new ActionSupport(mainWindow));
		registerAction(new ActionClose(mainWindow));
		registerAction(new ActionContextualHelp(mainWindow));
		registerAction(new ActionCopy(mainWindow));
		registerAction(new ActionCut(mainWindow));
		registerAction(new ActionDelete(mainWindow));
		registerAction(new ActionExit(mainWindow));
		registerAction(new ActionPrint(mainWindow));
		registerAction(new ActionInsertIntermediateResult(mainWindow));
		registerAction(new ActionInsertThreatReductionResult(mainWindow));
		registerAction(new ActionInsertLink(mainWindow));
		registerAction(new ActionInsertTarget(mainWindow));
		registerAction(new ActionInsertHumanWelfareTarget(mainWindow));
		registerAction(new ActionInsertDraftStrategy(mainWindow));
		registerAction(new ActionInsertStrategy(mainWindow));
		registerAction(new ActionInsertBiophysicalFactor(mainWindow));
		registerAction(new ActionInsertBiophysicalResult(mainWindow));
		registerAction(new ActionInsertDirectThreat(mainWindow));
		registerAction(new ActionInsertContributingFactor(mainWindow));
		registerAction(new ActionInsertAssumption(mainWindow));
		registerAction(new ActionSaveProjectAs(mainWindow));
		registerAction(new ActionHowToSave(mainWindow));

		registerAction(new ActionImportMpz(mainWindow));
		registerAction(new ActionImportMpf(mainWindow));
		registerAction(new ActionExportMpf(mainWindow));
		registerAction(new ActionExportXmpz2(mainWindow));
		registerAction(new ActionImportMiradiShareFile(mainWindow));
		registerAction(new ActionExportMiradiShareFile(mainWindow));
		registerAction(new ActionImportXmpz2(mainWindow));
		registerAction(new ActionExportMpf45Version(mainWindow));

		registerAction(new ActionProperties(mainWindow));
		registerAction(new ActionSaveImageJPEG(mainWindow));
		registerAction(new ActionSaveImagePng(mainWindow));
		registerAction(new ActionPaste(mainWindow));
		registerAction(new ActionPasteFactorContent(mainWindow));
		registerAction(new ActionPasteWithoutLinks(mainWindow));
		registerAction(new ActionRedo(mainWindow));
		registerAction(new ActionSelectAll(mainWindow));
		registerAction(new ActionClearAll(mainWindow));
		registerAction(new ActionSelectChain(mainWindow));
		registerAction(new ActionUndo(mainWindow));
		
		registerAction(new ActionViewSummary(mainWindow));
		registerAction(new ActionViewDiagram(mainWindow));
		registerAction(new ActionViewThreatMatrix(mainWindow));
		registerAction(new ActionViewPlanning(mainWindow));
		registerAction(new ActionViewBudget(mainWindow));
		registerAction(new ActionViewWorkPlan(mainWindow));
		registerAction(new ActionViewStrategicPlan(mainWindow));
		registerAction(new ActionViewMonitoring(mainWindow));
		registerAction(new ActionViewTargetViability(mainWindow));
		registerAction(new ActionViewOperationalPlan(mainWindow));
		registerAction(new ActionViewReports(mainWindow));
		
		registerAction(new ActionConfigureLayers(mainWindow));
		registerAction(new ActionShowSelectedChainMode(mainWindow));
		registerAction(new ActionShowFullModelMode(mainWindow));
		registerAction(new ActionZoomIn(mainWindow));
		registerAction(new ActionZoomOut(mainWindow));
		registerAction(new ActionZoomToFit(mainWindow));
		registerAction(new ActionNudgeUp(mainWindow));
		registerAction(new ActionNudgeDown(mainWindow));
		registerAction(new ActionNudgeLeft(mainWindow));
		registerAction(new ActionNudgeRight(mainWindow));
		registerAction(new ActionCreateDiagramMargin(mainWindow));
		registerAction(new ActionTreeCreateActivity(mainWindow));
		registerAction(new ActionTreeCreateMonitoringActivity(mainWindow));
		registerAction(new ActionTreeCreateRelevancyActivity(mainWindow));
		registerAction(new ActionTreeCreateRelevancyMonitoringActivity(mainWindow));
		registerAction(new ActionTreeMoveActivity(mainWindow));
		registerAction(new ActionTreeCreateMethod(mainWindow));
		registerAction(new ActionCreateTask(mainWindow));
		registerAction(new ActionCreateSameLevelTask(mainWindow));
		registerAction(new ActionTreeCreateIndicator(mainWindow));
		registerAction(new ActionTreeCreateObjective(mainWindow));

		registerAction(new ActionTreeCreateResourceAssignment(mainWindow));
		registerAction(new ActionTreeCreateExpenseAssignment(mainWindow));

		registerAction(new ActionCreateActivity(mainWindow));
		registerAction(new ActionDeleteActivity(mainWindow));
		registerAction(new ActionMoveActivity(mainWindow));
		registerAction(new ActionCreateResource(mainWindow));
		registerAction(new ActionDeleteResource(mainWindow));
		registerAction(new ActionPlanningCreationMenu(mainWindow));
		registerAction(new ActionWorkPlanningCreationMenu(mainWindow));
		registerAction(new ActionPlanningRowsEditor(mainWindow));
		registerAction(new ActionPlanningColumnsEditor(mainWindow));
		registerAction(new ActionWorkPlanBudgetCustomizeTableEditor(mainWindow));
		registerAction(new ActionEditAnalysisRows(mainWindow));
		registerAction(new ActionFilterWorkPlanByProjectResource(mainWindow));
		registerAction(new ActionExpandToMenu(mainWindow));
		registerAction(new ActionExpandToTarget(mainWindow));
		registerAction(new ActionExpandToHumanWelfareTarget(mainWindow));
		registerAction(new ActionExpandToKeyEcologicalAttribute(mainWindow));
		registerAction(new ActionExpandToIndicator(mainWindow));
		registerAction(new ActionExpandToMeasurement(mainWindow));
		registerAction(new ActionExpandToFutureStatus(mainWindow));
		registerAction(new ActionExpandToStrategy(mainWindow));
		registerAction(new ActionExpandToActivity(mainWindow));
		registerAction(new ActionPlanningCustomizeDialogPopup(mainWindow));
		registerAction(new ActionCreateCustomFromCurrentTreeTableConfiguration(mainWindow));

		registerAction(new ActionCreateIndicator(mainWindow));
		registerAction(new ActionCloneIndicator(mainWindow));
		
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
		registerAction(new ActionCreateIndicatorMeasurement(mainWindow));

		registerAction(new ActionCreateSubAssumption(mainWindow));
		registerAction(new ActionDeleteSubAssumption(mainWindow));

		registerAction(new ActionTeamCreateMember(mainWindow));
		registerAction(new ActionDeleteTeamMember(mainWindow));

		registerAction(new ActionAddAssignment(mainWindow));
		registerAction(new ActionRemoveAssignment(mainWindow));
		registerAction(new ActionCreateAccountingCode(mainWindow));
		registerAction(new ActionDeleteAccountingCode(mainWindow));
		registerAction(new ActionImportAccountingCodes(mainWindow));
		registerAction(new ActionCreateCategoryOne(mainWindow));
		registerAction(new ActionDeleteCategoryOne(mainWindow));
		registerAction(new ActionCreateCategoryTwo(mainWindow));
		registerAction(new ActionDeleteCategoryTwo(mainWindow));
		registerAction(new ActionCreateFundingSource(mainWindow));
		registerAction(new ActionDeleteFundingSource(mainWindow));
		registerAction(new ActionCreateExpense(mainWindow));
		registerAction(new ActionDeleteExpense(mainWindow));
		registerAction(new ActionCreateIucnRedlistSpecies(mainWindow));
		registerAction(new ActionDeleteIucnRedlistSpecies(mainWindow));
		registerAction(new ActionCreateOtherNotableSpecies(mainWindow));
		registerAction(new ActionDeleteOtherNotableSpecies(mainWindow));
		registerAction(new ActionCreateAudience(mainWindow));
		registerAction(new ActionDeleteAudience(mainWindow));
		registerAction(new ActionRemoveMiradiShareAssociation(mainWindow));

		registerAction(new ActionDeleteWorkPlanNode(mainWindow));
		registerAction(new ActionPreferences(mainWindow));
		registerAction(new ActionToggleSpellChecker(mainWindow));
		registerAction(new ActionTreeNodeUp(mainWindow));
		registerAction(new ActionTreeNodeDown(mainWindow));
		
		registerAction(new ActionCreateIncomingJunction(mainWindow));
		registerAction(new ActionCreateOutgoingJunction(mainWindow));
		
		registerAction(new ActionExportRtf(mainWindow));
		
		registerAction(new ActionImportXslTemplate(mainWindow));
		registerAction(new ActionRunXslTemplate(mainWindow));
		registerAction(new ActionDeleteXslTemplate(mainWindow));
		registerAction(new ActionExportXslTemplate(mainWindow));
		registerAction(new ActionCreateReportTemplate(mainWindow));
		registerAction(new ActionDeleteReportTemplate(mainWindow));
		registerAction(new ActionRunReportTemplate(mainWindow));
		registerAction(new ActionCreateTaggedObjectSet(mainWindow));
		registerAction(new ActionDeleteTaggedObjectSet(mainWindow));
		registerAction(new ActionEditTaggedObjectSet(mainWindow));
		registerAction(new ActionManageFactorTagsFromMenu(mainWindow));
		registerAction(new ActionManageFactorTags(mainWindow));
		registerAction(new ActionCreateNamedTaggedObjectSet(mainWindow));
		registerAction(new ActionLinkToMiradiShare(mainWindow));

		registerAction(new ActionCopyDiagramFactorFormat(mainWindow));
		registerAction(new ActionPasteDiagramFactorFormat(mainWindow));

		registerNonMenuJumpAction(new ActionJumpWelcomeCreateStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpWelcomeImportStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpSummaryWizardDefineProjectLeader(mainWindow));
		registerJumpMenuAction(new ActionJumpSummaryWizardRolesAndResponsibilities(mainWindow));
		registerJumpMenuAction(new ActionJumpSummaryWizardDefineTeamMembers(mainWindow));
		registerJumpMenuAction(new ActionJumpSummaryWizardDefineProjecScope(mainWindow));
		registerNonMenuJumpAction(new ActionJumpSummaryWizardDefineProjectVision(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardDefineTargetsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDiagramWizardProjectScopeStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDiagramWizardReviewAndModifyTargetsStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardHumanWelfareTargetsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpTargetViabilityOverviewStep(mainWindow));
		registerJumpMenuAction(new ActionJumpTargetViabilityMethodChoiceStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpTargetViability3Step(mainWindow));
		registerNonMenuJumpAction(new ActionJumpTargetStressesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardIdentifyDirectThreatStep(mainWindow));
		registerJumpMenuAction(new ActionJumpThreatMatrixOverviewStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardIdentifyIndirectThreatStep(mainWindow));
		registerJumpMenuAction(new ActionJumpAssessStakeholders(mainWindow));
		registerNonMenuJumpAction(new ActionJumpAnalyzeProjectCapacity(mainWindow));
		registerNonMenuJumpAction(new ActionJumpArticulateCoreAssumptions(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardReviewModelAndAdjustStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpGroundTruthRevise(mainWindow));
		registerNonMenuJumpAction(new ActionJumpPlanningOverviewStep(mainWindow));
		registerJumpMenuAction(new ActionJumpStrategicPlanDevelopGoalStep(mainWindow));
		registerJumpMenuAction(new ActionJumpSelectChainStep(mainWindow));
		registerJumpMenuAction(new ActionJumpStrategicPlanDevelopObjectivesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpRankDraftStrategiesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardResultsChainSelectStrategyStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpActivitiesAndActionPlan(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDevelopDraftStrategiesStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpAnalyzeResourcesFeasibilityAndRisk(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardDefineAudienceStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDefineAudiences(mainWindow));
		registerJumpMenuAction(new ActionJumpMonitoringWizardDefineIndicatorsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpSelectAppropriateMethods(mainWindow));
		registerNonMenuJumpAction(new ActionJumpPlanDataStorage(mainWindow));
		registerNonMenuJumpAction(new ActionJumpShorttermPlans(mainWindow));
		registerNonMenuJumpAction(new ActionJumpScheduleOverviewStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDefineTasks(mainWindow));
//		registerAction(new ActionJumpFinancialOverviewStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpTeamRoles(mainWindow));
		registerJumpMenuAction(new ActionJumpImplementWorkPlan(mainWindow));
		registerNonMenuJumpAction(new ActionJumpRefinePlans(mainWindow));
		registerNonMenuJumpAction(new ActionJumpAnalyzeData(mainWindow));
		registerNonMenuJumpAction(new ActionJumpAnalyzeStrategies(mainWindow));
		registerNonMenuJumpAction(new ActionJumpCommunicateResults(mainWindow));
		registerNonMenuJumpAction(new ActionJumpAdaptAndMonitorPlans(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDocument(mainWindow));
		registerNonMenuJumpAction(new ActionJumpShare(mainWindow));
		registerNonMenuJumpAction(new ActionJumpCreate(mainWindow));
		registerNonMenuJumpAction(new ActionJumpCloseTheLoop(mainWindow));
		registerJumpMenuAction(new ActionJumpPlanningWizardFinalizeStrategicPlanStep(mainWindow));
		registerJumpMenuAction(new ActionJumpPlanningWizardFinalizeMonitoringPlanStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpPlanningWizardDevelopOperationalPlan(mainWindow));
		registerNonMenuJumpAction(new ActionJumpStrategicPlanViewAllGoals(mainWindow));
		registerNonMenuJumpAction(new ActionJumpStrategicPlanViewAllObjectives(mainWindow));
		registerNonMenuJumpAction(new ActionJumpStrategicPlanHowToConstructStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpMonitoringWizardEditIndicatorsStep(mainWindow));
//		registerAction(new ActionJumpMonitoringPlanOverviewStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpMonitoringWizardSelectMethodsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpWorkPlanAssignResourcesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpWorkPlanDevelopMethodsAndTasksStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpWorkPlanAssignResourcesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardReviewModelAndAdjustStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpThreatRatingWizardCheckTotalsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpEditAllStrategiesStep(mainWindow));
		registerJumpMenuAction(new ActionJumpWorkPlanDevelopActivitiesAndTasksStep(mainWindow));
		registerJumpMenuAction(new ActionJumpWorkPlanDevelopMethodsAndTasksStep(mainWindow));
		registerJumpMenuAction(new ActionJumpDevelopMap(mainWindow));
		registerJumpMenuAction(new ActionJumpDiagramWizardCreateInitialModelStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpFinalizeConceptualModel(mainWindow));
		registerJumpMenuAction(new ActionJumpAssessResources(mainWindow));
		registerJumpMenuAction(new ActionJumpAssessRisks(mainWindow));
		registerJumpMenuAction(new ActionJumpPlanProjectLifespan(mainWindow));
		registerJumpMenuAction(new ActionJumpDevelopSchedule(mainWindow));
		registerJumpMenuAction(new ActionJumpBudgetWizardAccountingAndFunding(mainWindow));
		registerJumpMenuAction(new ActionJumpBudgetWizardAnalyzingWorkPlanData(mainWindow));
		registerJumpMenuAction(new ActionJumpDevelopFundingProposals(mainWindow));
		registerJumpMenuAction(new ActionJumpObtainFinancing(mainWindow));
		registerJumpMenuAction(new ActionJumpImplementStrategicAndMonitoringPlans(mainWindow));
		registerNonMenuJumpAction(new ActionJumpPlanningWizardImplementPlans(mainWindow));
		
		registerJumpMenuAction(new ActionOpenStandardsConceptualizeParentMenu(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsConceptualizeProcessStep1a(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsConceptualizeProcessStep1b(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsConceptualizeProcessStep1c(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsConceptualizeProcessStep1d(mainWindow));
		
		registerJumpMenuAction(new ActionOpenStandardsPlanActionsAndMonitoringParentMenu(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsPlanActionsAndMonitoringProcessStep2a(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsPlanActionsAndMonitoringProcessStep2b(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsPlanActionsAndMonitoringProcessStep2c(mainWindow));
		
		registerJumpMenuAction(new ActionOpenStandardsImplementActionsAndMonitoringParentMenu(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsImplementActionsAndMonitoringProcessStep3a(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsImplementActionsAndMonitoringProcessStep3b(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsImplementActionsAndMonitoringProcessStep3c(mainWindow));
		
		registerJumpMenuAction(new ActionOpenStandardsAnalyzeUseAndAdaptParentMenu(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4a(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4b(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsAnalyzeUseAndAdaptProcessStep4c(mainWindow));
		
		registerJumpMenuAction(new ActionOpenStandardsCaptureAndShareLearningParentMenu(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsCaptureAndShareLearningProcessStep5a(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsCaptureAndShareLearningProcessStep5b(mainWindow));
		registerJumpMenuAction(new ActionOpenStandardsCaptureAndShareLearningProcessStep5c(mainWindow));
		
		registerNonMenuJumpAction(new ActionJumpScheduleOverviewStep(mainWindow));
//		registerAction(new ActionJumpFinancialOverviewStep(mainWindow));
		registerNonMenuJumpAction(new ActionJumpBudgetFutureDemo(mainWindow));
		registerAction(new ActionConfigureExport(mainWindow));
		registerAction(new ActionDatabasesDemo(mainWindow));
		registerAction(new ActionReportsDemo(mainWindow));
		registerNonMenuJumpAction(new ActionJumpDiagramOverviewStep(mainWindow));
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
		registerAction(new ActionInsertScopeBox(mainWindow));
		registerAction(new ActionInsertGroupBox(mainWindow));
		registerAction(new ActionCreateConceptualModel(mainWindow));
		registerAction(new ActionDeleteConceptualModel(mainWindow));
		registerAction(new ActionArrangeConceptualModel(mainWindow));
		registerAction(new ActionCreatePlanningViewEmptyConfiguration(mainWindow));
		registerAction(new ActionCreatePlanningViewPrefilledConfiguration(mainWindow));
		registerAction(new ActionCreatePlanningViewConfigurationMenu(mainWindow));
		registerAction(new ActionDeletePlanningViewConfiguration(mainWindow));
		registerAction(new ActionDeletePlanningViewTreeNode(mainWindow));
		registerAction(new ActionGroupBoxAddFactor(mainWindow));
		registerAction(new ActionGroupBoxRemoveFactor(mainWindow));
		registerAction(new ActionEditMethods(mainWindow));
		registerAction(new ActionCreateMethod(mainWindow));
		registerAction(new ActionDeleteMethod(mainWindow));

		registerAction(new ActionBringForward(mainWindow));
		registerAction(new ActionBringToFront(mainWindow));
		registerAction(new ActionSendBackward(mainWindow));
		registerAction(new ActionSendToBack(mainWindow));

		registerAction(new ActionEditObjectiveIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditGoalIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditObjectiveStrategyActivityRelevancyList(mainWindow));
		registerAction(new ActionEditGoalStrategyActivityRelevancyList(mainWindow));
		registerAction(new ActionEditIndicatorStrategyActivityRelevancyList(mainWindow));
		registerAction(new ActionEditStrategyGoalRelevancyList(mainWindow));
		registerAction(new ActionEditStrategyObjectiveRelevancyList(mainWindow));
		registerAction(new ActionEditStrategyIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditActivityObjectiveRelevancyList(mainWindow));
		registerAction(new ActionEditActivityGoalRelevancyList(mainWindow));
		registerAction(new ActionEditActivityIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditOutputGoalRelevancyList(mainWindow));
		registerAction(new ActionEditOutputObjectiveRelevancyList(mainWindow));
		registerAction(new ActionEditOutputIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditAssumptionIndicatorRelevancyList(mainWindow));
		registerAction(new ActionEditSubAssumptionIndicatorRelevancyList(mainWindow));

		registerAction(new ActionDeleteGroupBox(mainWindow));
		registerAction(new ActionCreateSubTarget(mainWindow));
		registerAction(new ActionDeleteSubTarget(mainWindow));
		registerAction(new ActionDiagramProperties(mainWindow));

		registerAction(new ActionCreateProgressReport(mainWindow));
		registerAction(new ActionCreateExtendedProgressReport(mainWindow));
		registerAction(new ActionDeleteProgressReport(mainWindow));
		registerAction(new ActionDeleteExtendedProgressReport(mainWindow));

		registerAction(new ActionCreateResultReport(mainWindow));
		registerAction(new ActionDeleteResultReport(mainWindow));

		registerAction(new ActionEditStrategyOutputs(mainWindow));
		registerAction(new ActionEditTaskOutputs(mainWindow));
		registerAction(new ActionCreateOutput(mainWindow));
		registerAction(new ActionDeleteOutput(mainWindow));
		registerAction(new ActionCloneOutput(mainWindow));

		registerAction(new ActionDeleteOrganization(mainWindow));
		registerAction(new ActionCreateOrganization(mainWindow));
		registerAction(new ActionExportTable(mainWindow));
		registerAction(new ActionCreateProgressPercent(mainWindow));
		registerAction(new ActionDeleteProgressPercent(mainWindow));
		registerAction(new ActionViewLegacyTncStrategyRanking(mainWindow));
		registerAction(new ActionDeleteLegacyTncStrategyRanking(mainWindow));
		registerAction(new ActionActivityMoveUp(mainWindow));
		registerAction(new ActionActivityMoveDown(mainWindow));
		registerAction(new ActionShowStressBubble(mainWindow));
		registerAction(new ActionHideStressBubble(mainWindow));
		registerAction(new ActionShowActivityBubble(mainWindow));
		registerAction(new ActionHideActivityBubble(mainWindow));
		registerAction(new ActionShowSubAssumptionBubble(mainWindow));
		registerAction(new ActionHideSubAssumptionBubble(mainWindow));
		registerAction(new ActionShowCurrentWizardFileName(mainWindow));
		registerAction(new ActionExpandAllRows(mainWindow));
		registerAction(new ActionCollapseAllRows(mainWindow));
		registerAction(new ActionCreateFutureStatus(mainWindow));

		registerAction(new ActionShowAllResourceAssignmentRows(mainWindow));
		registerAction(new ActionHideAllResourceAssignmentRows(mainWindow));
		registerAction(new ActionShowAllExpenseAssignmentRows(mainWindow));
		registerAction(new ActionHideAllExpenseAssignmentRows(mainWindow));
	}
	
	public MiradiAction get(Class c)
	{
		Object action = actions.get(c);
		if(action == null)
			throw new RuntimeException("Unknown action: " + c);
		
		return (MiradiAction)action;
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
		HashMap<MiradiAction,Boolean> newStates = new HashMap<MiradiAction, Boolean>();
		Collection actualActions = actions.values();
		Iterator iter = actualActions.iterator();
		while(iter.hasNext())
		{
			MiradiAction action = (MiradiAction)iter.next();
			boolean shouldBeEnabled = action.shouldBeEnabled();
			newStates.put(action, shouldBeEnabled);
		}
		SwingUtilities.invokeLater(new DelayedActionSetEnabled(newStates));
	}
	
	class DelayedActionSetEnabled implements Runnable
	{
		public DelayedActionSetEnabled(HashMap<MiradiAction,Boolean> newStatesToApply)
		{
			newStates = newStatesToApply;
		}

		public void run()
		{
			for(MiradiAction action : newStates.keySet())
			{
				action.setEnabled(newStates.get(action));
			}
		}
		
		HashMap<MiradiAction,Boolean> newStates;
	}
	
	private void registerAction(MiradiAction action)
	{
		actions.put(action.getClass(), action);
	}

	private void registerNonMenuJumpAction(AbstractJumpAction action)
	{
		if (action.hasCode())
			throw new RuntimeException("Attempting to register a menu action as a non menu action: " + action.getClass().getSimpleName());
				
		registerAction(action);
	}
	
	private void registerJumpMenuAction(AbstractJumpMenuAction menuAction)
	{
		if (!menuAction.hasCode())
			throw new RuntimeException("Attempting to register a non menu action as a menu action: " + menuAction.getClass().getSimpleName());
		
		codeToJumpMenuActionMap.put(menuAction.getCode(), menuAction);
		registerAction(menuAction);
	}
	
	public AbstractJumpMenuAction getJumpMenuAction(String code)
	{
		return codeToJumpMenuActionMap.get(code);
	}

	private Map<Class, MiradiAction> actions;
	private HashMap<String, AbstractJumpMenuAction> codeToJumpMenuActionMap;
}
