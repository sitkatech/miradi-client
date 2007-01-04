/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionHelpAdaptiveManagement;
import org.conservationmeasures.eam.actions.ActionHelpAgileSoftware;
import org.conservationmeasures.eam.actions.ActionHelpCMPStandards;
import org.conservationmeasures.eam.actions.ActionHelpComingAttractions;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionExportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionHelpButtonExamples;
import org.conservationmeasures.eam.actions.ActionHelpCredits;
import org.conservationmeasures.eam.actions.ActionHelpButtonMoreInfo;
import org.conservationmeasures.eam.actions.ActionHelpButtonWorkshop;
import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionProjectSaveAs;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeInterventions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
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
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
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
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringFocus;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringOverview;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpResultsChains;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpViewAllObjectives;
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
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.ResourcePropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.NullDoer;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;
import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;

abstract public class UmbrellaView extends JPanel implements CommandExecutedListener
{
	public UmbrellaView(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		nullDoer = new NullDoer();
		actionToDoerMap = new HashMap();
		addUmbrellaDoersToMap();
	}
	
	abstract public String cardName();
	
	public void jump(Class stepMarker) throws Exception
	{
		return;
	}

	public void becomeActive() throws Exception
	{
		if(isActive)
			EAM.logWarning("UmbrellaView.becomeActive was already active: " + getClass().getName());
		getProject().addCommandExecutedListener(this);
		isActive = true;
	}
	
	public void becomeInactive() throws Exception
	{
		if(!isActive)
			EAM.logWarning("UmbrellaView.becomeActive was not active: " + getClass().getName());
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
	
	public JComponent getToolBar()
	{
		return toolBar;
	}
	
	protected void setToolBar(JComponent newToolBar)
	{
		toolBar = newToolBar;
	}
	
	public BufferedImage getImage()
	{
		throw new RuntimeException("This view doesn't support getImage");
	}
	
	public JComponent getPrintableComponent()
	{
		throw new RuntimeException("This view doesn't support getPrintableComponent");
	}
	
	
	public void showResourcePropertiesDialog(EAMObject object) throws Exception
	{
		ModelessDialogPanel propertiesPanel = createPanelForDialog(object);
		if(propertiesPanel == null)
			return;
		
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(mainWindow, propertiesPanel, propertiesPanel.getPanelDescription());
		dlg.addWindowListener(new ObjectPropertiesDialogWindowEventHandler());
		showFloatingPropertiesDialog(dlg);
	}
	
	public EAMObject getSelectedObject()
	{
		return null;
	}
	
	public TaskTreeTablePanel getTaskTreeTablePanel()
	{
		return null;
	}
	
	class ObjectPropertiesDialogWindowEventHandler extends WindowAdapter
	{

		public void windowClosed(WindowEvent e)
		{
			super.windowClosed(e);
			closeActivePropertiesDialog();
		}

	}
	
	private ModelessDialogPanel createPanelForDialog(EAMObject object) throws Exception
	{	
		return new ResourcePropertiesPanel(getProject(), object.getId());
	}

	public void showFloatingPropertiesDialog(ModelessDialogWithClose newDialog)
	{
		if(activePropertiesDlg != null)
			activePropertiesDlg.dispose();
		
		activePropertiesDlg = newDialog;
		activePropertiesPanel = (ModelessDialogPanel)newDialog.getWrappedPanel();
		activePropertiesDlg.pack();
		Utilities.centerDlg(activePropertiesDlg);
		activePropertiesDlg.setVisible(true);
	}
	
	protected UiLabel createScreenShotLabel()
	{
		UiLabel label = new UiLabel("Demo Screen Shot");
		label.setBorder(new LineBorder(Color.BLACK));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	////////////////////////////////////////////////////////////
	// these doers are available in this class
	
	private void addUmbrellaDoersToMap()
	{
		addDoerToMap(ActionAbout.class, new AboutDoer());
		addDoerToMap(ActionHelpComingAttractions.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpAdaptiveManagement.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpAgileSoftware.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpCMPStandards.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpCredits.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonExamples.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonMoreInfo.class, new HelpButtonDoer());
		addDoerToMap(ActionHelpButtonWorkshop.class, new HelpButtonDoer());
		
		addDoerToMap(ActionNewProject.class, new NewProject());
		addDoerToMap(ActionProjectSaveAs.class, new SaveAsProjectDoer());
		addDoerToMap(ActionClose.class, new Close());
		addDoerToMap(ActionExit.class, new Exit());
		addDoerToMap(ActionUndo.class, new Undo());
		addDoerToMap(ActionRedo.class, new Redo());
		addDoerToMap(ActionPreferences.class, new Preferences());
		addDoerToMap(ActionExportZippedProjectFile.class, new ExportZippedProjectFileDoer());
		addDoerToMap(ActionImportZippedProjectFile.class, new ImportZippedProjectFileDoer());
		addDoerToMap(ActionImportTncCapWorkbook.class, new ImportTncCapWorkbookDoer());
		
		addDoerToMap(ActionViewSummary.class, new ViewSummary());
		addDoerToMap(ActionViewDiagram.class, new ViewDiagram());
		addDoerToMap(ActionViewThreatMatrix.class, new ViewThreatMatrix());
		addDoerToMap(ActionViewBudget.class, new ViewBudget());
		addDoerToMap(ActionViewWorkPlan.class, new ViewWorkPlan());
		addDoerToMap(ActionViewMap.class, new ViewMap());
		addDoerToMap(ActionViewImages.class, new ViewImages());
		addDoerToMap(ActionViewSchedule.class, new ViewSchedule());
		addDoerToMap(ActionViewStrategicPlan.class, new ViewStrategicPlan());
		addDoerToMap(ActionViewMonitoring.class, new ViewMonitoring());
		
		addJumpDoerToMap(ActionJumpSelectTeam.class);
		addJumpDoerToMap(ActionJumpDesignateLeader.class);
		addJumpDoerToMap(ActionJumpDevelopCharter.class);
		
		addJumpDoerToMap(ActionJumpDefineScope.class);
		addJumpDoerToMap(ActionJumpEstablishVision.class);
		addJumpDoerToMap(ActionJumpIdentifyTargets.class);
		addJumpDoerToMap(ActionJumpDescribeTargets.class);
		
		addJumpDoerToMap(ActionJumpIdentifyDirectThreats.class);
		addJumpDoerToMap(ActionJumpRankDirectThreats.class);
		addJumpDoerToMap(ActionJumpIdentifyContributingFactors.class);
		addJumpDoerToMap(ActionJumpAssessStakeholders.class);
		addJumpDoerToMap(ActionJumpAnalyzeProjectCapacity.class);
		addJumpDoerToMap(ActionJumpMonitoringFocus.class);
		
		addJumpDoerToMap(ActionJumpArticulateCoreAssumptions.class);
		addJumpDoerToMap(ActionJumpCreateModel.class);
		addJumpDoerToMap(ActionJumpGroundTruthRevise.class);
		
		addJumpDoerToMap(ActionJumpDevelopTargetGoals.class);
		addJumpDoerToMap(ActionJumpIdentifyStrategies.class);
		addJumpDoerToMap(ActionJumpDevelopObjectives.class);
		
		addJumpDoerToMap(ActionJumpRankDraftStrategies.class);
		addJumpDoerToMap(ActionJumpResultsChains.class);
		addJumpDoerToMap(ActionJumpActivitiesAndActionPlan.class);
		addJumpDoerToMap(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class);
		
		addJumpDoerToMap(ActionJumpDetermineNeeds.class);
		addJumpDoerToMap(ActionJumpDefineAudiences.class);
		
		addJumpDoerToMap(ActionJumpDefineIndicators.class);
		addJumpDoerToMap(ActionJumpEditIndicators.class);
		addJumpDoerToMap(ActionJumpSelectAppropriateMethods.class);
		addJumpDoerToMap(ActionJumpPlanDataStorage.class);
		
		addJumpDoerToMap(ActionJumpMonitoringOverview.class);
		
		addJumpDoerToMap(ActionJumpShorttermPlans.class);
		addJumpDoerToMap(ActionJumpDevelopActivities.class);
		addJumpDoerToMap(ActionJumpDefineTasks.class);
		addJumpDoerToMap(ActionJumpDevelopBudgets.class);
		addJumpDoerToMap(ActionJumpTeamRoles.class);
		addJumpDoerToMap(ActionJumpRefinePlans.class);
		addJumpDoerToMap(ActionJumpImplementPlans.class);
		
		addJumpDoerToMap(ActionJumpAnalyzeData.class);
		addJumpDoerToMap(ActionJumpAnalyzeInterventions.class);
		addJumpDoerToMap(ActionJumpCommunicateResults.class);
		
		addJumpDoerToMap(ActionJumpAdaptAndMonitorPlans.class);
		
		addJumpDoerToMap(ActionJumpDocument.class);
		addJumpDoerToMap(ActionJumpShare.class);
		addJumpDoerToMap(ActionJumpCreate.class);
		
		addJumpDoerToMap(ActionJumpCloseTheLoop.class);
		
		addJumpDoerToMap(ActionJumpViewAllGoals.class);
		addJumpDoerToMap(ActionJumpViewAllObjectives.class);
		addJumpDoerToMap(ActionJumpStratPlanWelcome.class);
	}
	
	private void addJumpDoerToMap(Class actionClass)
	{
		addDoerToMap(actionClass, new JumpDoer(actionClass));
	}
	
	public void addDoerToMap(Class actionClass, Doer doer)
	{
		actionToDoerMap.put(actionClass, doer);
	}
	
	public Doer getDoer(Class actionClass)
	{
		Doer doer = (Doer)actionToDoerMap.get(actionClass);
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

	public void commandUndone(CommandExecutedEvent event)
	{
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	void closeActivePropertiesDialogIfWeDeletedItsObject(Command rawCommand)
	{
		if(activePropertiesDlg == null)
			return;
		
		if(!rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			return;
		
		CommandDeleteObject cmd = (CommandDeleteObject)rawCommand;
		EAMObject objectBeingEdited = activePropertiesPanel.getObject();
		if(objectBeingEdited == null)
			return;
		if(cmd.getObjectType() != objectBeingEdited.getType())
			return;
		if(cmd.getObjectId() != objectBeingEdited.getId())
			return;
		
		closeActivePropertiesDialog();
	}
	
	public void closeActivePropertiesDialog()
	{
		if(activePropertiesPanel != null && activePropertiesDlg.isDisplayable())
			activePropertiesDlg.dispose();
		activePropertiesPanel = null;
		activePropertiesDlg = null;
	}

	private MainWindow mainWindow;
	private NullDoer nullDoer;
	private JComponent toolBar;
	private HashMap actionToDoerMap;
	private boolean isActive;
	
	private ModelessDialogPanel activePropertiesPanel;
	private ModelessDialogWithClose activePropertiesDlg;
 
}
