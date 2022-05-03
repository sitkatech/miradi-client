/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.umbrella;

import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;
import org.miradi.actions.*;
import org.miradi.actions.jump.*;
import org.miradi.actions.views.*;
import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.dialogs.base.AbstractDialogWithClose;
import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.TableExporter;
import org.miradi.views.Doer;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.NullDoer;
import org.miradi.views.diagram.Print;
import org.miradi.views.diagram.doers.*;
import org.miradi.views.umbrella.doers.*;
import org.miradi.views.workplan.doers.HideAllExpenseAssignmentsDoer;
import org.miradi.views.workplan.doers.HideAllResourceAssignmentsDoer;
import org.miradi.views.workplan.doers.ShowAllExpenseAssignmentsDoer;
import org.miradi.views.workplan.doers.ShowAllResourceAssignmentsDoer;
import org.miradi.wizard.SkeletonWizardStep;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class UmbrellaView extends JPanel implements CommandExecutedListener
{
	public UmbrellaView(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		nullDoer = new NullDoer();
		actionToDoerMap = new HashMap<Class, Doer>();
		addUmbrellaDoersToMap();
	}
	
	abstract public String cardName();

	abstract public MiradiTabContentsPanelInterface getCurrentTabPanel();

	public void setTabForStep(SkeletonWizardStep step)
	{
	}
	
	public void refresh() throws Exception
	{
		becomeInactive();
		becomeActive();
	}
	
	public void becomeActive() throws Exception
	{
		if(isActive)
			EAM.logWarning("UmbrellaView.becomeActive was already active: " + getClass().getName());
		getProject().addCommandExecutedListener(this);
		isActive = true;
		getMainWindow().clearStatusBar();
	}
	
	public void becomeInactive() throws Exception
	{
		if(!isActive)
			EAM.logWarning("UmbrellaView.becomeActive was not active: " + getClass().getName());
		
		closeActivePropertiesDialog();
		getProject().removeCommandExecutedListener(this);
		isActive = false;
		removeAll();
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	abstract public MiradiToolBar createToolBar();
	
	public boolean isImageAvailable()
	{
		return false;
	}
	
	public boolean isExportableTableAvailable()
	{
		return false;
	}
	
	public boolean isPrintable()
	{
		return false;
	}
	
	public boolean isRtfExportable()
	{
		return false;
	}
	
	public TableExporter getTableExporter() throws Exception
	{
		throw new RuntimeException("This view doesn't support getExportableTable");
	}
	
	public BufferedImage getImage(int scalePercent) throws Exception
	{
		throw new RuntimeException("This view doesn't support getImage");
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		throw new RuntimeException("This view doesn't support getPrintableComponent");
	}
	
	public void exportRtf(RtfWriter writer) throws Exception
	{
		throw new RuntimeException("This view doesn't support exportRtf");
	}
	
	public BaseObject getSelectedObject()
	{
		return null;
	}
	
	public void showFloatingPropertiesDialog(AbstractDialogWithClose newDialog)
	{
		closeActivePropertiesDialog();
		
		activePropertiesDlg = newDialog;
		activePropertiesDlg.becomeActive();
		activePropertiesDlg.pack();
		Utilities.centerDlg(activePropertiesDlg);
		activePropertiesDlg.setVisible(true);
	}

	protected UiLabel createScreenShotLabel()
	{
		UiLabel label = new PanelTitleLabel("Demo Screen Shot");
		label.setBorder(new LineBorder(Color.BLACK));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	////////////////////////////////////////////////////////////
	// these doers are available in this class
	
	private void addUmbrellaDoersToMap()
	{
		addDoerToMap(ActionAbout.class, new AboutDoer());
		addDoerToMap(ActionAboutCMP.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpComingAttractions.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpAdaptiveManagement.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpAgileSoftware.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpCMPStandards.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpCredits.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonExamples.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonMoreInfo.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonWorkshop.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonSupport.class, new HelpButtonDoer());
		
		addDoerToMap(ActionSaveProjectAs.class, new SaveProjectAsDoer());
		addDoerToMap(ActionHowToSave.class, new HowToSaveDoer());
		addDoerToMap(ActionClose.class, new Close());
		addDoerToMap(ActionExit.class, new Exit());
		addDoerToMap(ActionUndo.class, new UndoDoer());
		addDoerToMap(ActionRedo.class, new RedoDoer());
		addDoerToMap(ActionPreferences.class, new PreferencesPopupDoer());
		addDoerToMap(ActionToggleSpellChecker.class, new ToggleSpellCheckerDoer());

		addDoerToMap(ActionImportMpz.class, new ImportMpzDoer());
		addDoerToMap(ActionImportMpf.class, new ImportMpfProjectDoer());
		addDoerToMap(ActionExportMpf.class, new ExportMpfDoer());
		addDoerToMap(ActionExportXmpz2.class, new Xmpz2ProjectExportDoer());
		addDoerToMap(ActionImportMiradiShareFile.class, new ImportXmpz2Doer());
		addDoerToMap(ActionExportMiradiShareFile.class, new Xmpz2ProjectExportDoer());
		addDoerToMap(ActionExportMpf45Version.class, new ExportMpf45VersionDoer());
		addDoerToMap(ActionImportXmpz2.class, new ImportXmpz2Doer());
		addDoerToMap(ActionExportRtf.class, new ExportRtfDoer());
		addDoerToMap(ActionExportTable.class, new TabDelimitedTableExportDoer());
		addDoerToMap(ActionSaveImageJPEG.class, new SaveImageJPEGDoer());
		addDoerToMap(ActionSaveImagePng.class, new SaveImagePngDoer());
		addDoerToMap(ActionConfigureExport.class, new HelpButtonDoer());
		addDoerToMap(ActionDatabasesDemo.class, new HelpButtonDoer());
		addDoerToMap(ActionReportsDemo.class, new HelpButtonDoer());
		
		addDoerToMap(ActionEditMethods.class, new EditMethodsDoer());
		addDoerToMap(ActionCreateMethod.class, new CreateMethodDoer());
		addDoerToMap(ActionDeleteMethod.class, new DeleteMethodDoer());

		addDoerToMap(ActionCreateProgressReport.class, new CreateProgressReportDoer());
		addDoerToMap(ActionCreateExtendedProgressReport.class, new CreateExtendedProgressReportDoer());
		addDoerToMap(ActionDeleteProgressReport.class, new DeleteProgressReportDoer());
		addDoerToMap(ActionDeleteExtendedProgressReport.class, new DeleteExtendedProgressReportDoer());

		addDoerToMap(ActionCreateResultReport.class, new CreateResultReportDoer());
		addDoerToMap(ActionDeleteResultReport.class, new DeleteResultReportDoer());

		addDoerToMap(ActionEditStrategyOutputs.class, new EditStrategyOutputsDoer());
		addDoerToMap(ActionEditTaskOutputs.class, new EditTaskOutputsDoer());
		addDoerToMap(ActionCreateOutput.class, new CreateOutputDoer());
		addDoerToMap(ActionDeleteOutput.class, new DeleteOutputDoer());

		addDoerToMap(ActionCreateProgressPercent.class, new CreateProgressPercentDoer());
		addDoerToMap(ActionDeleteProgressPercent.class, new DeleteProgressPercentDoer());
		
		addDoerToMap(ActionCreateProgressPercent.class, new CreateProgressPercentDoer());
		addDoerToMap(ActionDeleteProgressPercent.class, new DeleteProgressPercentDoer());
		
		addDoerToMap(ActionViewSummary.class, new SwitchToSummaryViewDoer());
		addDoerToMap(ActionViewDiagram.class, new SwitchToDiagramViewDoer());
		addDoerToMap(ActionViewThreatMatrix.class, new SwitchToThreatRatingViewDoer());
		addDoerToMap(ActionViewPlanning.class, new SwitchToPlanningViewDoer());
		addDoerToMap(ActionViewMap.class, new SwitchToMapViewDoer());
		addDoerToMap(ActionViewImages.class, new SwitchToLibraryViewDoer());
		addDoerToMap(ActionViewSchedule.class, new SwitchToScheduleViewDoer());
		addDoerToMap(ActionViewTargetViability.class, new SwitchToTargetViabilityViewDoer());
		addDoerToMap(ActionViewReports.class, new SwitchToReportViewDoer());
		addDoerToMap(ActionViewWorkPlan.class, new SwitchToWorkPlanViewDoer());
		
		addDoerToMap(ActionEditObjectiveIndicatorRelevancyList.class, new EditObjectiveIndicatorRelevancyListDoer());
		addDoerToMap(ActionEditGoalIndicatorRelevancyList.class, new EditGoalIndicatorRelevancyListDoer());
		addDoerToMap(ActionEditObjectiveStrategyActivityRelevancyList.class, new EditObjectiveStrategyActivityRelevancyListDoer());
		addDoerToMap(ActionEditGoalStrategyActivityRelevancyList.class, new EditGoalStrategyActivityRelevancyListDoer());
		addDoerToMap(ActionEditIndicatorStrategyActivityRelevancyList.class, new EditIndicatorStrategyActivityRelevancyListDoer());

		addDoerToMap(ActionEditOutputGoalRelevancyList.class, new EditOutputGoalRelevancyListDoer());		
		addDoerToMap(ActionEditOutputObjectiveRelevancyList.class, new EditOutputObjectiveRelevancyListDoer());		
		addDoerToMap(ActionEditOutputIndicatorRelevancyList.class, new EditOutputIndicatorRelevancyListDoer());		
		
		addDoerToMap(ActionEditAnalyticalQuestionIndicatorRelevancyList.class, new EditAnalyticalQuestionIndicatorRelevancyListDoer());
		addDoerToMap(ActionEditAssumptionIndicatorRelevancyList.class, new EditAssumptionIndicatorRelevancyListDoer());

		addDoerToMap(ActionEditStrategyObjectiveRelevancyList.class, new EditStrategyObjectiveRelevancyListDoer());
		addDoerToMap(ActionEditStrategyGoalRelevancyList.class, new EditStrategyGoalRelevancyListDoer());
		addDoerToMap(ActionEditStrategyIndicatorRelevancyList.class, new EditStrategyIndicatorRelevancyListDoer());
		addDoerToMap(ActionEditActivityObjectiveRelevancyList.class, new EditActivityObjectiveRelevancyListDoer());
		addDoerToMap(ActionEditActivityGoalRelevancyList.class, new EditActivityGoalRelevancyListDoer());
		addDoerToMap(ActionEditActivityIndicatorRelevancyList.class, new EditActivityIndicatorRelevancyListDoer());

		addDoerToMap(ActionViewLegacyTncStrategyRanking.class, new ViewLegacyTncStrategyRankingDoer());
		addDoerToMap(ActionDeleteLegacyTncStrategyRanking.class, new DeleteLegacyTncStrategyRankingDoer());
		
		addDoerToMap(ActionExpandAllRows.class, new ExpandAllRowsDoer());
		addDoerToMap(ActionCollapseAllRows.class, new CollapseAllRowsDoer());
		
		addDoerToMap(ActionAddAssignment.class, new CreateAssignmentDoer());
		addDoerToMap(ActionRemoveAssignment.class, new DeleteResourceAssignmentDoer());
		addDoerToMap(ActionCreateExpense.class, new CreateExpenseDoer());
		addDoerToMap(ActionDeleteExpense.class, new DeleteExpenseDoer());
		
		addDoerToMap(ActionCreateIucnRedlistSpecies.class, new CreateIucnRedlistSpeciesDoer());
		addDoerToMap(ActionDeleteIucnRedlistSpecies.class, new DeleteIucnRedlistSpeciesDoer());
		
		addDoerToMap(ActionCreateOtherNotableSpecies.class, new CreateOtherNotableSpeciesDoer());
		addDoerToMap(ActionDeleteOtherNotableSpecies.class, new DeleteOtherNotableSpeciesDoer());
		
		addDoerToMap(ActionCreateAudience.class, new CreateAudienceDoer());
		addDoerToMap(ActionDeleteAudience.class, new DeleteAudienceDoer());

		addDoerToMap(ActionRemoveMiradiShareAssociation.class, new RemoveMiradiShareAssociationDoer());

		addDoerToMap(ActionPrint.class, new Print());
		addDoerToMap(ActionShowCurrentWizardFileName.class, new ShowCurrentWizardFileNameDoer());
		addDoerToMap(ActionInvokeDashboard.class, new DashboardDoer());
		addDoerToMap(ActionLinkToMiradiShare.class, new LinkToMiradiShareDoer());

		addDoerToMap(ActionWizardNext.class, new WizardNextDoer());
		addDoerToMap(ActionWizardPrevious.class, new WizardPreviousDoer());

		addDoerToMap(ActionShowAllResourceAssignmentRows.class, new ShowAllResourceAssignmentsDoer());
		addDoerToMap(ActionHideAllResourceAssignmentRows.class, new HideAllResourceAssignmentsDoer());
		addDoerToMap(ActionShowAllExpenseAssignmentRows.class, new ShowAllExpenseAssignmentsDoer());
		addDoerToMap(ActionHideAllExpenseAssignmentRows.class, new HideAllExpenseAssignmentsDoer());

		addJumpDoerToMap(ActionJumpSummaryWizardDefineTeamMembers.class);
		addJumpDoerToMap(ActionJumpSummaryWizardDefineProjectLeader.class);
		addJumpDoerToMap(ActionJumpSummaryWizardRolesAndResponsibilities.class);
		
		addJumpDoerToMap(ActionJumpSummaryWizardDefineProjecScope.class);
		addJumpDoerToMap(ActionJumpSummaryWizardDefineProjectVision.class);
		addJumpDoerToMap(ActionJumpDiagramWizardDefineTargetsStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardProjectScopeStep.class);
		addJumpDoerToMap(ActionJumpTargetViabilityOverviewStep.class);
		addJumpDoerToMap(ActionJumpTargetViabilityMethodChoiceStep.class);
		addJumpDoerToMap(ActionJumpTargetViability3Step.class);
		addJumpDoerToMap(ActionJumpTargetStressesStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardHumanWelfareTargetsStep.class);
		
		addJumpDoerToMap(ActionJumpDiagramWizardIdentifyDirectThreatStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardLinkDirectThreatsToTargetsStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardCreateInitialModelStep.class);
		addJumpDoerToMap(ActionJumpThreatMatrixOverviewStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardIdentifyIndirectThreatStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardReviewAndModifyTargetsStep.class);
		addJumpDoerToMap(ActionJumpAssessStakeholders.class);
		addJumpDoerToMap(ActionJumpAnalyzeProjectCapacity.class);
		
		addJumpDoerToMap(ActionJumpArticulateCoreAssumptions.class);
		addJumpDoerToMap(ActionJumpDiagramWizardReviewModelAndAdjustStep.class);
		addJumpDoerToMap(ActionJumpGroundTruthRevise.class);
		
		addJumpDoerToMap(ActionJumpStrategicPlanDevelopGoalStep.class);
		addJumpDoerToMap(ActionJumpSelectChainStep.class);
		addJumpDoerToMap(ActionJumpStrategicPlanDevelopObjectivesStep.class);
		
		addJumpDoerToMap(ActionJumpRankDraftStrategiesStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardResultsChainSelectStrategyStep.class);
		addJumpDoerToMap(ActionJumpEditAllStrategiesStep.class);
		addJumpDoerToMap(ActionJumpActivitiesAndActionPlan.class);
		addJumpDoerToMap(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class);
		
		addJumpDoerToMap(ActionJumpDiagramWizardDefineAudienceStep.class);
		addJumpDoerToMap(ActionJumpDefineAudiences.class);
		
		addJumpDoerToMap(ActionJumpMonitoringWizardDefineIndicatorsStep.class);
		addJumpDoerToMap(ActionJumpMonitoringWizardEditIndicatorsStep.class);
		addJumpDoerToMap(ActionJumpSelectAppropriateMethods.class);
		addJumpDoerToMap(ActionJumpPlanDataStorage.class);
		
		addJumpDoerToMap(ActionJumpMonitoringWizardSelectMethodsStep.class);
		
		addJumpDoerToMap(ActionJumpBudgetWizardAccountingAndFunding.class);
		addJumpDoerToMap(ActionJumpBudgetWizardAnalyzingWorkPlanData.class);

		addJumpDoerToMap(ActionJumpPlanningOverviewStep.class);
		addJumpDoerToMap(ActionJumpPlanningWizardFinalizeStrategicPlanStep.class);
		addJumpDoerToMap(ActionJumpPlanningWizardFinalizeMonitoringPlanStep.class);
		addJumpDoerToMap(ActionJumpShorttermPlans.class);
		addJumpDoerToMap(ActionJumpScheduleOverviewStep.class);
		addJumpDoerToMap(ActionJumpDefineTasks.class);
		addJumpDoerToMap(ActionJumpTeamRoles.class);
		addJumpDoerToMap(ActionJumpRefinePlans.class);
		addJumpDoerToMap(ActionJumpImplementWorkPlan.class);
		
		addJumpDoerToMap(ActionJumpAnalyzeData.class);
		addJumpDoerToMap(ActionJumpAnalyzeStrategies.class);
		addJumpDoerToMap(ActionJumpCommunicateResults.class);
		
		addJumpDoerToMap(ActionJumpAdaptAndMonitorPlans.class);
		
		addJumpDoerToMap(ActionJumpDocument.class);
		addJumpDoerToMap(ActionJumpShare.class);
		addJumpDoerToMap(ActionJumpCreate.class);
		
		addJumpDoerToMap(ActionJumpCloseTheLoop.class);
		
		addJumpDoerToMap(ActionJumpStrategicPlanViewAllGoals.class);
		addJumpDoerToMap(ActionJumpStrategicPlanViewAllObjectives.class);
		addJumpDoerToMap(ActionJumpStrategicPlanHowToConstructStep.class);
		
		addJumpDoerToMap(ActionJumpDevelopDraftStrategiesStep.class);

		addJumpDoerToMap(ActionJumpWorkPlanAssignResourcesStep.class);
		addJumpDoerToMap(ActionJumpDiagramWizardReviewModelAndAdjustStep.class);
		addJumpDoerToMap(ActionJumpThreatRatingWizardCheckTotalsStep.class);

		addJumpDoerToMap(ActionJumpWorkPlanDevelopActivitiesAndTasksStep.class);
		addJumpDoerToMap(ActionJumpWorkPlanDevelopMethodsAndTasksStep.class);
		addJumpDoerToMap(ActionJumpWorkPlanAssignResourcesStep.class);
		addJumpDoerToMap(ActionJumpScheduleOverviewStep.class);
		addJumpDoerToMap(ActionJumpFinancialOverviewStep.class);
		addJumpDoerToMap(ActionJumpBudgetFutureDemo.class);
		addJumpDoerToMap(ActionJumpDiagramOverviewStep.class);
	}
	
	public void addJumpDoerToMap(Class actionClass)
	{
		addDoerToMap(actionClass, new JumpDoer(actionClass));
	}
	
	public void addDoerToMap(Class actionClass, Doer doer)
	{
		actionToDoerMap.put(actionClass, doer);
	}
	
	public Doer getDoer(Class actionClass)
	{
		Doer doer = actionToDoerMap.get(actionClass);
		if(doer == null)
			doer = nullDoer;
		
		doer.setView(this);
		doer.setProject(getProject());
		return doer;
	}
	
	protected ViewData getViewData() throws Exception
	{
		ViewData ourViewData = getProject().getViewData(cardName());
		return ourViewData;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		closeActivePropertiesDialogIfWeDeletedItsObject(event.getCommand());
	}

	private void closeActivePropertiesDialogIfWeDeletedItsObject(Command rawCommand)
	{
		if(!rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			return;
		
		CommandDeleteObject cmd = (CommandDeleteObject)rawCommand;
		
		if(isActivePropertiesDialogEditing(cmd.getObjectRef()))
			closeActivePropertiesDialog();
	}
	
	private boolean isActivePropertiesDialogEditing(ORef ref)
	{
		if(activePropertiesDlg == null)
			return false;
		
		ModelessDialogPanel panel = safeGetWrappedModelessDialogPanel();
		if(panel == null)
			return false;
		
		BaseObject objectBeingEdited = panel.getObject();
		if(objectBeingEdited == null)
			return false;
		
		return (ref.equals(objectBeingEdited.getRef()));
	}
	
	private ModelessDialogPanel safeGetWrappedModelessDialogPanel()
	{
		JPanel wrappedPanel = activePropertiesDlg.getWrappedPanel();
		if(wrappedPanel instanceof ModelessDialogPanel)
			return (ModelessDialogPanel)wrappedPanel;
		
		return null;
	}

	public void closeActivePropertiesDialog()
	{
		if(activePropertiesDlg != null && activePropertiesDlg.isDisplayable())
		{
			activePropertiesDlg.setVisible(false);
			activePropertiesDlg.dispose();
		}
		activePropertiesDlg = null;
	}

	public void addActionToListIfDoerAvailable(ArrayList<Action> actions, Class actionClass)
	{
		Doer actionDoer = getDoer(actionClass);
		if (actionDoer.isAvailable())
			actions.add(getActions().get(actionClass));
	}

	protected void forceLayoutSoSplittersWork()
	{
		getTopLevelAncestor().validate();
	}

	private MainWindow mainWindow;
	private NullDoer nullDoer;
	private HashMap<Class, Doer> actionToDoerMap;
	private boolean isActive;
	private AbstractDialogWithClose activePropertiesDlg;
}
