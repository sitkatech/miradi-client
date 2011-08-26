/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.martus.swing.HyperlinkHandler;
import org.martus.util.DirectoryLock;
import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeReader;
import org.miradi.actions.Actions;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.dialogfields.AbstractWorkPlanStringMapEditorDoer;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogs.ProjectCorruptionDialog;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.FutureVersionException;
import org.miradi.exceptions.OldVersionException;
import org.miradi.exceptions.UnknownCommandException;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.main.menu.MainMenuBar;
import org.miradi.objecthelpers.ColorsFileLoader;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objecthelpers.TwoLevelEntry;
import org.miradi.objects.Assignment;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.project.ProjectRepairer;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.TableRowHeightModeQuestion;
import org.miradi.utils.DefaultHyperlinkHandler;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.utils.MiradiBackgroundWorkerThread;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.SplitterPositionSaverAndGetter;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.library.LibraryView;
import org.miradi.views.map.MapView;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.reports.ReportsView;
import org.miradi.views.schedule.ScheduleView;
import org.miradi.views.summary.SummaryPlanningWorkPlanSubPanel;
import org.miradi.views.summary.SummaryView;
import org.miradi.views.targetviability.TargetViabilityView;
import org.miradi.views.threatmatrix.ThreatMatrixView;
import org.miradi.views.umbrella.Definition;
import org.miradi.views.umbrella.DefinitionCommonTerms;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.ViewSplitPane;
import org.miradi.views.workplan.WorkPlanView;
import org.miradi.wizard.SkeletonWizardStep;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WizardTitlePanel;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

public class MainWindow extends JFrame implements ClipboardOwner, SplitterPositionSaverAndGetter
{
	public static MainWindow create() throws Exception
	{
		ProjectServer projectServer = new ProjectServer();
		return new MainWindow(projectServer);
	}
	
	public MainWindow(ProjectServer projectServerToUse) throws Exception
	{
		this(new Project(projectServerToUse));
	}

	public MainWindow(Project projectToUse) throws Exception
	{
		preferences = new AppPreferences();
		project = projectToUse;
		setFocusCycleRoot(true);
		wizardManager = new WizardManager(this);
		hyperlinkHandler = new DefaultHyperlinkHandler(this);
		setLanguage(null);

		database = getProject().getDatabase();
	}
	
	public void start(String[] args) throws Exception
	{
		List<String> commandLineArguments = Arrays.asList(args);
		
		if(ResourcesHandler.isRunningFromInsideJar())
		{
			if(!VersionConstants.hasValidVersion())
				EAM.logWarning("Invalid or missing Miradi version number");
			if(!VersionConstants.hasValidTimestamp())
				EAM.logWarning("Invalid or missing Miradi build identifier");
		}
		
		File appPreferencesFile = getPreferencesFile();
		preferences.load(appPreferencesFile);

		if(commandLineArguments.contains("--remote"))
		{
			// NOTE: This is code for testing the fat client
			// The server must be running on fxa.org, and 
			// /var/local/MiradiServer/project/Test
			// must be a valid Miradi project directory
			setRemoteDataLocation("http://fxa.org:7000/MiradiServer/");
		}
		else
		{
			setLocalDataLocation(EAM.getHomeDirectory());
		}
		
		ensureFontSizeIsSet();
		
		humanWelfareModeHandler = new HumanWelfareTargetModeChangeHandler();
		lowMemoryHandler = new WarnLowMemoryHandler();
		refreshWizardHandler = new RefreshWizardHandler();
		
		getProject().addCommandExecutedListener(humanWelfareModeHandler);
		getProject().addCommandExecutedListener(refreshWizardHandler);
		getProject().addCommandExecutedListener(lowMemoryHandler);
		
		ToolTipManager.sharedInstance().setInitialDelay(TOOP_TIP_DELAY_MILLIS);
		ToolTipManager.sharedInstance().setReshowDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		
		loadCustomAppColors();
		addWindowListener(new WindowEventHandler());
		setSize(new Dimension(900, 700));

		setExtendedState(ICONIFIED);
		setSize(0,0);
		setLocation(-100, -100);
		setVisible(true);
		if(!commandLineArguments.contains("--nosplash"))
		{
			InitialSplashPanel splash = new InitialSplashPanel(this);
			splash.showAsOkDialog();
			String languageCode = splash.getSelectedLanguageCode();
			if(languageCode != null && !languageCode.equals("en"))
				setLanguage(languageCode);

			getAppPreferences().setLanguageCode(languageCode);
		}

		new SampleInstaller(getAppPreferences()).installSampleProjects();

		setIconImage(new MiradiResourceImageIcon("images/appIcon.png").getImage());
		EAM.logDebug("\n\n\n");

		WizardTitlePanel wizardTitlePanel = new WizardTitlePanel(this);
		mainMenuBar = new MainMenuBar(this);
		toolBarBox = new ToolBarContainer();
		mainStatusBar = new MainStatusBar();
		updateTitle();
		setJMenuBar(mainMenuBar);
		getContentPane().setLayout(new BorderLayout());

		wizardPanel = createWizardPanel(wizardTitlePanel);
		
		noProjectView = new NoProjectView(this);
		summaryView = new SummaryView(this);
		diagramView = new DiagramView(this);
		threatMatrixView = new ThreatMatrixView(this);
		mapView = new MapView(this);
		calendarView = new ScheduleView(this);
		libraryView = new LibraryView(this);
		targetViabilityView = new TargetViabilityView(this);
		planningView = new PlanningView(this);
		workPlanView = new WorkPlanView(this);
		reportView = new ReportsView(this);

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(summaryView, summaryView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(threatMatrixView, threatMatrixView.cardName());
		viewHolder.add(mapView, mapView.cardName());
		viewHolder.add(calendarView, calendarView.cardName());
		viewHolder.add(libraryView, libraryView.cardName());
		viewHolder.add(targetViabilityView, targetViabilityView.cardName());
		viewHolder.add(planningView, planningView.cardName());
		viewHolder.add(workPlanView, workPlanView.cardName());
		viewHolder.add(reportView, reportView.cardName());

		getWizardManager().setOverViewStep(NoProjectView.getViewName());
		updateActionStates();

		setSize(getSizeFromPreferences());
		setLocation(preferences.getMainWindowXPosition(), preferences.getMainWindowYPosition());
		setExtendedState(NORMAL);
		if(preferences.getIsMaximized())
		{
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
		
		safelySavePreferences();
	}

	public boolean isSpellCheckerActive()
	{
		if(!getProject().isOpen())
			return false;
		
		return getAppPreferences().getIsSpellCheckEnabled() && isSpellCheckPossible();
	}
	
	public boolean isSpellCheckPossible()
	{
		String languageCode = getProject().getMetadata().getData(ProjectMetadata.TAG_PROJECT_LANGUAGE);
		return isSpellCheckPossible(languageCode);
	}
	
	private boolean isSpellCheckPossible(String languageCode)
	{
		if(languageCode == null || languageCode.length() == 0)
			return true;
		
		return (languageCode.equals("en"));
	}

	private Dimension getSizeFromPreferences()
	{
		int MINIMUM_WIDTH = 200;
		int MINIMUM_HEIGHT = 100;
		int width = Math.max(MINIMUM_WIDTH, preferences.getMainWindowWidth());
		int height = Math.max(MINIMUM_HEIGHT, preferences.getMainWindowHeight());
		return new Dimension(width, height);
	}

	private void ensureFontSizeIsSet()
	{
		if(preferences.getPanelFontSize() == 0)
			setDataPanelFontSize(new JLabel().getFont().getSize());
	}
	
	private void setLanguage(String languageCode) throws Exception
	{
		Miradi.switchToLanguage(languageCode);
		actions = new Actions(this);
	}

	private void loadCustomAppColors() throws Exception
	{
		try
		{
			File home = EAM.getHomeDirectory();
			UnicodeReader reader = new UnicodeReader(new File(home, "colors.txt"));
			ColorsFileLoader colorsLoader = new ColorsFileLoader();
			TwoLevelEntry[] colors = colorsLoader.load(reader);
			for (int i = 0; i < colors.length; ++i)
			{
				TwoLevelEntry colorEntry = colors[i];
				if (colorEntry.getEntryCode().equals("WizardBorder"))
					getAppPreferences().setWizardTitleBackground(colorEntry.getEntryLabel());

				if (colorEntry.getEntryCode().equals("WizardPanel"))
					getAppPreferences().setWizardBackgroundColor(colorEntry.getEntryLabel());

				if (colorEntry.getEntryCode().equals("WizardSidebar"))
					getAppPreferences().setWizardSidebarBackgroundColor(colorEntry.getEntryLabel());

				if (colorEntry.getEntryCode().equals("DataBorder"))
					getAppPreferences().setDarkControlPanelBackgroundColor(colorEntry.getEntryLabel());

				if (colorEntry.getEntryCode().equals("DataPanel"))
					getAppPreferences().setDataPanelBackgroundColor(colorEntry.getEntryLabel());

				if (colorEntry.getEntryCode().equals("ControlBar"))
					getAppPreferences().setControlPanelBackgroundColor(colorEntry.getEntryLabel());
			}
			
			EAM.logVerbose("Loaded colors from file");
		}
		catch (IOException e)
		{
			EAM.logVerbose("No color file found");
		}
	}
		
	public void hideDivider()
	{
		getContentPane().removeAll();
		if (spliterPane!=null)
		{
			spliterPane = null;
		}
		
		getContentPane().add(wizardPanel, BorderLayout.CENTER);
	}
	
	public void showDivider()
	{
		getContentPane().removeAll();
		
		spliterPane = new ViewSplitPane(this, wizardPanel, viewHolder);

		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(spliterPane, BorderLayout.CENTER);
		getContentPane().add(getMainStatusBar(), BorderLayout.AFTER_LAST_LINE);
	}
	
	public void forceViewSplitterToMiddle()
	{
		if (spliterPane == null)
			return;
		spliterPane.setDividerLocation(spliterPane.getHeight()/2);
	}
	
	public int getDividerLocation()
	{
		if (spliterPane!=null)
			return spliterPane.getDividerLocation();
		return 0;
	}

	private WizardPanel createWizardPanel(WizardTitlePanel wizardTitlePanel) throws Exception
	{
		return new WizardPanel(this, wizardTitlePanel);
	}
	
	public WizardPanel getWizard()
	{
		return wizardPanel;
	}

	public AppPreferences getAppPreferences()
	{
		return preferences;
	}
	
	private File getPreferencesFile()
	{
		File appPreferencesFile = new File(EAM.getHomeDirectory(), APP_PREFERENCES_FILENAME);
		return appPreferencesFile;
	}

	public HyperlinkHandler getHyperlinkHandler()
	{
		return hyperlinkHandler;
	}

	private JComponent createCenteredView(JComponent viewToCenter)
	{
		Box centered = Box.createHorizontalBox();
		centered.add(Box.createHorizontalGlue());
		centered.add(viewToCenter);
		centered.add(Box.createHorizontalGlue());
		return centered;
	}
	
	public UmbrellaView getCurrentView()
	{
		return currentView;
	}
	
	private void setCurrentView(UmbrellaView view) throws Exception
	{
		if(currentView != null)
		{
			currentView.becomeInactive();
			possiblyLogIncorrectCommandListenerCount();
		}

		CardLayout layout = (CardLayout)viewHolder.getLayout();
		layout.show(viewHolder, view.cardName());
		currentView = view;
		project.switchToView(view.cardName());
		existingCommandListenerCount = getProject().getCommandListenerCount();
		preventActionUpdates();
		try
		{
			currentView.becomeActive();
			rebuildToolBar();
		}
		finally
		{
			allowActionUpdates();
			updateMenuOptions();
			updateActionsAndStatusBar();
		}
	}

	private void possiblyLogIncorrectCommandListenerCount()
	{
		if(getProject().getCommandListenerCount() != existingCommandListenerCount)
		{
			EAM.logDebug("CommandListener count wrong during view change (was= " + existingCommandListenerCount + ", is= " + getProject().getCommandListenerCount() +  " )");
			getProject().logDebugCommandListeners();
		}
	}
	
	private void logOrphanedCommandListeners()
	{
		if(getProject().getCommandListenerCount() != 0)
		{
			EAM.logError("CommandListener count wrong (was " + getProject().getCommandListenerCount() + ")");
			getProject().logDebugCommandListeners();
		}
	}

	private void updateMenuOptions()
	{
		mainMenuBar.updateMenuOptions();
	}

	public void rebuildToolBar()
	{
		toolBarBox.clear();
		SwingUtilities.invokeLater(new ToolBarUpdater());
	}
	
	class ToolBarUpdater implements Runnable
	{
		public void run()
		{
			UmbrellaView view = getCurrentView();
			if(view == null)
				return;
			MiradiToolBar toolBar = view.createToolBar();
			if(toolBar == null)
				throw new RuntimeException("View must have toolbar");

			toolBar.setBackground(AppPreferences.getControlPanelBackgroundColor());
			toolBarBox.setToolBar(toolBar);
			toolBarBox.invalidate();
			toolBarBox.validate();
			invalidate();
			validate();
			repaint();
		}
	}

	public Project getProject()
	{
		return project;
	}
	
	public ProjectServer getDatabase()
	{
		return database;
	}
	
	public WizardManager getWizardManager()
	{
		return wizardManager;
	}
	
	public DiagramComponent getCurrentDiagramComponent()
	{
		if(diagramView == null)
			return null;
		return diagramView.getCurrentDiagramComponent();
	}
	
	public DiagramModel getDiagramModel()
	{
		if (getCurrentDiagramComponent() == null)
			return null;
		
		return getCurrentDiagramComponent().getDiagramModel();
	}
	
	public Actions getActions()
	{
		return actions;
	}
	
	public void setLocalDataLocation(File dataDirectory) throws Exception
	{
		database.setLocalDataLocation(dataDirectory);
	}
	
	public void setRemoteDataLocation(String remoteLocation) throws Exception
	{
		database.setRemoteDataLocation(remoteLocation);
	}
	
	static class AlreadyHandledException extends Exception
	{
	}

	public void createOrOpenProject(String projectName)
	{
		preventActionUpdates();
		project.beginCommandSideEffectMode();
		try
		{
			createOrOpenProjectInBackground(projectName);
			logExceptionsInsideProjectDir();
			
			repairProject();
			refreshWizard();

			validate();
			updateTitle();
			updateStatusBar();
			getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
		}
		catch(UserCanceledException e)
		{
			EAM.notifyDialog(EAM.text("Cancelled"));
		}
		catch(AlreadyHandledException e)
		{
			try
			{
				closeProject();
			}
			catch(Exception exceptionDuringClose)
			{
				throw new RuntimeException("Unable to close the partially-opened project", exceptionDuringClose);
			}
		}
		catch(UnknownCommandException e)
		{
			EAM.errorDialog(EAM.text("Unknown Command\nYou are probably trying to load an old project " +
					"that contains obsolete commands that are no longer supported"));
		}
		catch(DirectoryLock.AlreadyLockedException e)
		{
			EAM.errorDialog(EAM.text("That project is in use by another copy of this application"));
		}
		catch(FutureVersionException e)
		{
			EAM.errorDialog(EAM.text("That project cannot be opened because it was created by a newer version of this application"));
		}
		catch(OldVersionException e)
		{
			EAM.errorDialog(EAM.text("That project cannot be opened until it is migrated to the current data format"));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unknown error prevented opening that project"));
			try
			{
				project.close();
				getWizardManager().setOverViewStep(NoProjectView.getViewName());
			}
			catch(Exception e1)
			{
				EAM.panic(e1);
			}
		}
		finally
		{
			allowActionUpdates();
			updateActionsAndStatusBar();
			project.endCommandSideEffectMode();
		}
	}

	private void createOrOpenProjectInBackground(String projectName) throws Exception
	{
		String title = EAM.text("Create Project");
		if(getDatabase().isExistingProject(projectName))
			title = EAM.text("Open Project");
		ProgressDialog progressDialog = new ProgressDialog(this, title);
		ProjectOpenWorker worker = new ProjectOpenWorker(progressDialog, project, projectName);
		progressDialog.doWorkInBackgroundWhileShowingProgress(worker);

	}

	private static class ProjectOpenWorker extends MiradiBackgroundWorkerThread
	{
		public ProjectOpenWorker(ProgressInterface progressInterfaceToUse, Project projectToUse, String projectNameToUse)
		{
			super(progressInterfaceToUse);
			
			project = projectToUse;
			projectName = projectNameToUse;
		}
		
		@Override
		protected void doRealWork() throws Exception
		{
			project.createOrOpenWithDefaultObjectsAndDiagramHelp(projectName, getProgressIndicator());
			getProgressIndicator().finished();
		}
		
		private Project project;
		private String projectName;
	}

	private void repairProject() throws Exception
	{
		ProjectRepairer repairer = new ProjectRepairer(project);
		quarantineOrphans(repairer);
		repairer.repairProblemsWherePossible();
		scanForSeriousCorruption(repairer);
	}

	private void quarantineOrphans(ProjectRepairer repairer) throws Exception
	{
		int quarantinedCount = repairer.quarantineOrphans();
		if(quarantinedCount == 0)
			return;
		
		EAM.notifyDialog(EAM.text("This project has been optimized to remove data that is no longer needed.\n"));
	}

	private void scanForSeriousCorruption(ProjectRepairer repairer) throws Exception
	{
		HashMap<ORef, ORefSet> rawProblems = repairer.getListOfMissingObjects();
		if(rawProblems.size() == 0)
			return;

		String title = EAM.text("Project Corruption Detected");
		String bodyText = EAM.text(
				"Miradi has detected one or more problems with this project " + 
				"which could cause errors or further damage in the future. " +
				"\n" +
				"The specific problems are listed below, to help assess the " +
				"severity of the damage. " +
				"\n" +
				"We recommend that you close this project and contact the " +
				"Miradi support team so they can safely repair this project.");

		String listOfProblems = "";
		for(ORef missingRef : rawProblems.keySet())
		{
			ORefSet referrers = rawProblems.get(missingRef);
			String typeName = ObjectType.getUserFriendlyObjectTypeName(getProject(), missingRef.getObjectType());
			listOfProblems += "Missing " + typeName + " " + missingRef + " referred to by " + referrers.toString() + "\n";
		}
		
		if(!ProjectCorruptionDialog.askUserWhetherToOpen(this, title, bodyText, listOfProblems))
			throw new AlreadyHandledException();
	}

	private void logExceptionsInsideProjectDir()
	{
		File projectDir = getDatabase().getCurrentLocalProjectDirectory();
		EAM.setExceptionLoggingDestination(new File(projectDir, EAM.EXCEPTIONS_LOG_FILE_NAME));
	}
	
	public DiagramView getDiagramView()
	{
		return diagramView;
	}
	
	public ThreatMatrixView getThreatView()
	{
		return threatMatrixView;
	}
	
	public PlanningView getPlanningView()
	{
		return planningView;
	}

	public void closeProject() throws Exception
	{
		EAM.logDebug(getMemoryStatistics());
		project.close();
		getWizardManager().setOverViewStep(NoProjectView.getViewName());

		updateTitle();
		getMainStatusBar().clear();
	}
	
	public void refreshWizard() throws Exception
	{
		if (getWizard() == null)
			return;
		
		SkeletonWizardStep step = getWizardManager().getCurrentStep();
		setViewForStep(step);
		setTabForStep(step);
		getWizard().setContents(step);
		getWizard().refresh();
		validate();
	}
	
	private void setTabForStep(SkeletonWizardStep step)
	{
		currentView.setTabForStep(step);
	}

	private void setViewForStep(SkeletonWizardStep step) throws Exception
	{
		if(getCurrentView() != null && step.getViewName().equals(getCurrentView().cardName()))
			return;
		
		setCurrentView(step.getViewName());
	}

	public void updateStatusBar()
	{
		// NOTE: We don't have a real status bar right now
	}
	
	public void updateToolBar()
	{
		toolBarBox.updateToolBar();
	}

	public void exitNormally()
	{
		try
		{
			closeProject();
			getProject().removeCommandExecutedListener(humanWelfareModeHandler);
			getProject().removeCommandExecutedListener(lowMemoryHandler);
			getProject().removeCommandExecutedListener(refreshWizardHandler);
			getProject().dispose();
			savePreferences();
			getCurrentView().becomeInactive();
			logOrphanedCommandListeners();
			System.exit(0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			System.exit(1);
		}
	}

	private void warnOnceIfLowOnMemory()
	{
		if(hasMemoryWarningBeenShown)
			return;
		
		Runtime runtime = Runtime.getRuntime();
		if(runtime.totalMemory() < runtime.maxMemory())
			return;
		
		final long REASONABLE_MEMORY_CUSHION = 5 * 1024 * 1024;
		if(runtime.freeMemory() > REASONABLE_MEMORY_CUSHION)
			return;
		
		EAM.logWarning(getMemoryStatistics());
		
		EAM.errorDialog("<html><strong>Miradi is running low on memory.</strong><br><br> " +
				"Please exit and restart Miradi, and report this to the Miradi team at<br>" +
				"<a href='mailto://support@miradi.org'>support@miradi.org</a>");
		
		hasMemoryWarningBeenShown = true;
	}

	private String getMemoryStatistics()
	{
		Runtime runtime = Runtime.getRuntime();
		String memoryStatistics = "\nMemory Statistics:\n" +
						"  Free: " + runtime.freeMemory() + "\n" + 
						"  Used: " + runtime.totalMemory() + "\n" +
						"  Max:  " + runtime.maxMemory();
		return memoryStatistics;
	}

	public void setStatusBarWarningMessage(String warningMessage)
	{
		getMainStatusBar().setWarningStatus(warningMessage);
	}
	
	public void updatePlanningDateRelatedStatus()
	{
		try
		{
			if (areStartEndDateFlipped())
				setStartEndDateWarningStatus();
			else if (areAnyProjectResourceFiltersOn())
				getMainStatusBar().setWarningStatus(EAM.text("Project Resource Filter Is On"));
			else if (hasNonMatchingFiscalYearStartMonth(getProject()))
				getMainStatusBar().setWarningStatus(EAM.text("Existing data for a different fiscal year is being excluded"));
			else if (isDataOutsideOfcurrentProjectDateRange())
				getMainStatusBar().setWarningStatus(("WorkPlan/Financial data outside project begin/end dates will not be shown"));
			else
				clearStatusBar();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			clearStatusBar();
		}
	}
	
	private boolean areAnyProjectResourceFiltersOn() throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), AbstractWorkPlanStringMapEditorDoer.getTabSpecificModelIdentifier());
		StringStringMap tableSettingsMap = tableSettings.getTableSettingsMap();
		String refs = tableSettingsMap.get(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

		return new ORefList(refs).size() > 0;
	}
	
	private boolean areStartEndDateFlipped()
	{
		return getProject().getProjectCalendar().arePlanningStartAndEndDatesFlipped();
	}

	private void setStartEndDateWarningStatus()
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%startDate", getProject().getProjectCalendar().getPlanningStartDate());
		tokenReplacementMap.put("%endDate", getProject().getProjectCalendar().getPlanningEndDate());
		String warningMessage = EAM.substitute(EAM.text("Start date (currently %startDate) must be earlier than end date (currently %endDate)"), tokenReplacementMap);
		getMainStatusBar().setWarningStatus(warningMessage);
	}
	
	private static boolean hasNonMatchingFiscalYearStartMonth(Project projectToUse) throws Exception
	{
		ORefList assignmentRefs = new ORefList();
		assignmentRefs.addAll(projectToUse.getAssignmentPool().getORefList());
		assignmentRefs.addAll(projectToUse.getPool(ExpenseAssignment.getObjectType()).getRefList());
		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(projectToUse, assignmentRefs.get(index));
			if (assignment.hasAnyYearDateUnitWithWrongStartMonth())
				return true;
		}
		
		return false;
	}
		
	public void clearStatusBar()
	{
		getMainStatusBar().clear();
	}
	
	private MainStatusBar getMainStatusBar()
	{
		return mainStatusBar;
	}

	private boolean isDataOutsideOfcurrentProjectDateRange()
	{
		String startDate = getProject().getProjectCalendar().getPlanningStartDate();
		String endDate = getProject().getProjectCalendar().getPlanningEndDate();

		if (startDate.trim().length() <= 0 || endDate.trim().length() <= 0)
			return false;
		
		MultiCalendar multiStartDate = MultiCalendar.createFromIsoDateString(startDate);
		MultiCalendar multiEndDate = MultiCalendar.createFromIsoDateString(endDate);
		
		if (multiStartDate.after(multiEndDate))
			return false;

		return SummaryPlanningWorkPlanSubPanel.hasDataOutsideOfProjectDateRange(getProject());
	}

	private void updateAfterCommand(CommandExecutedEvent event)
	{
		try
		{
			if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME))
			{
				refreshWizard();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error switching view");
		}
		
		updateActionsAndStatusBar();
	}

	public void updateActionsAndStatusBar()
	{
		updateActionStates();
		updateStatusBar();
		updateToolBar();
	}

	public void updateActionStates()
	{
		if(shouldPreventActionUpdates())
			return;

		actions.updateActionStates();
	}

	private boolean shouldPreventActionUpdates()
	{
		if(preventActionUpdatesCount > 0)
			return true;
		
		return getProject().isInTransaction();
	}
	
	public void preventActionUpdates()
	{
		++preventActionUpdatesCount;
		EAM.logVerbose("preventActionsUpdates: " + preventActionUpdatesCount);
	}
	
	public void allowActionUpdates()
	{
		if(preventActionUpdatesCount <= 0)
			throw new RuntimeException("Calls to prevent/allowActionUpdates not nested properly");
		--preventActionUpdatesCount;
		EAM.logVerbose("allowActionsUpdates: " + preventActionUpdatesCount);
	}

	private void setCurrentView(String viewName) throws Exception
	{
		setCurrentView(getView(viewName));
	}
	
	public UmbrellaView getView(String viewName)
	{
		if(viewName.equals(summaryView.cardName()))
			return summaryView;
		
		else if(viewName.equals(diagramView.cardName()))
			return diagramView;
		
		else if(viewName.equals(noProjectView.cardName()))
			return noProjectView;
		
		else if(viewName.equals(threatMatrixView.cardName()))
			return threatMatrixView;
		
		else if(viewName.equals(mapView.cardName()))
			return mapView;
		
		else if(viewName.equals(calendarView.cardName()))
			return calendarView;
		
		else if(viewName.equals(libraryView.cardName()))
			return libraryView;
		
		else if (viewName.equals(targetViabilityView.cardName()))
			return targetViabilityView;
		
		else if (viewName.equals(planningView.cardName()))
			return planningView;
		
		else if (viewName.equals(workPlanView.cardName()))
			return workPlanView;
		
		else if (viewName.equals(reportView.cardName()))
			return reportView;

		else
		{
			EAM.logError("MainWindow.switchToView: Unknown view: " + viewName);
			return summaryView;
		}
	}
	
	public void safelySavePreferences()
	{
		try
		{
			savePreferences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to save preferences"));
		}
	}
	
	public void savePreferences() throws Exception
	{
		boolean isMaximized = false;
		if((getExtendedState() & MAXIMIZED_BOTH) != 0)
			isMaximized = true;
		
		preferences.setIsMaximized(isMaximized);
		if(!isMaximized)
		{
			preferences.setMainWindowHeigth(getHeight());
			preferences.setMainWindowWidth(getWidth());
			preferences.setMainWindowXPosition(getLocation().x);
			preferences.setMainWindowYPosition(getLocation().y);
		}
		preferences.save(getPreferencesFile());
		getCurrentView().refresh();
	}
	
	public void setBooleanPreference(String genericTag, boolean state)
	{
		preferences.setBoolean(genericTag, state);
		if (getCurrentDiagramComponent()!=null)
			getCurrentDiagramComponent().updateDiagramComponent();
		repaint();
	}
	
	public boolean getBooleanPreference(String genericTag)
	{
		return preferences.getBoolean(genericTag);
	}
	
	public Color getColorPreference(String colorTag)
	{
		return preferences.getColor(colorTag);
	}
	
	public void setColorPreference(String colorTag, Color colorToUse)
	{
		preferences.setColor(colorTag, colorToUse);
		repaint();
	}

	public void saveSplitterLocation(String name, int location)
	{
		if(name == null)
			return;
		preferences.setTaggedInt(name, location);
	}
	
	public int getSplitterLocation(String name)
	{
		return preferences.getTaggedInt(name);
	}

	public Font getUserDataPanelFont()
	{
		ChoiceItem fontFamily = new FontFamiliyQuestion().findChoiceByCode(getDataPanelFontFamily());
		int fontSize = getDataPanelFontSizeWithDefault();
		return FontFamiliyQuestion.createFont(fontFamily, fontSize);
	}

	public int getDataPanelFontSizeWithDefault()
	{
		int size = preferences.getPanelFontSize();
		if (size == 0)
			return getContentPane().getFont().getSize();
		return size;
	}
	
	public int getDataPanelFontSize()
	{
		return preferences.getPanelFontSize();
	}
	
	public void setDataPanelFontSize(int fontSize)
	{
		preferences.setPanelFontSize(fontSize);
	}
	
	public String getDataPanelFontFamily()
	{
		return preferences.getPanelFontFamily();
	}
	
	public void setDataPanelFontFamily(String fontFamily)
	{
		preferences.setPanelFontFamily(fontFamily);
	}
	
	public int getWizardFontSize()
	{
		return preferences.getWizardFontSize();
	}
	
	public void setWizardFontSize(int fontSize)
	{
		preferences.setWizardFontSize(fontSize);
	}
	
	public String getWizardFontFamily()
	{
		return preferences.getWizardFontFamily();
	}
	
	public void setWizardFontFamily(String fontFamily)
	{
		preferences.setWizardFontFamily(fontFamily);
	}
	
	public void setRowHeightMode(String rowHeightMode)
	{
		preferences.setRowHeightMode(rowHeightMode);
	}

	public String getRowHeightModeString()
	{
		return preferences.getRowHeightMode();
	}

	public boolean isRowHeightModeManual()
	{
		return !isRowHeightModeAutomatic();
	}

	public boolean isRowHeightModeAutomatic()
	{
		return getRowHeightModeString().equals(TableRowHeightModeQuestion.AUTOMATIC_MODE_CODE);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) 
	{
	}
	
	private void updateTitle()
	{
		setTitle("Miradi - " + project.getFilename());
	}
	
	class WindowEventHandler extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent event)
		{
			try
			{
				FieldSaver.savePendingEdits();
				exitNormally();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				System.exit(1);
			}
		}
	}
	
	private class HumanWelfareTargetModeChangeHandler implements CommandExecutedListener
	{
		public void commandExecuted(CommandExecutedEvent event)
		{
			if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE))
				updateMenuOptions();
		}
	}
	
	private class RefreshWizardHandler implements CommandExecutedListener
	{
		public void commandExecuted(CommandExecutedEvent event)
		{
			updateAfterCommand(event);
		}
	}
	
	private class WarnLowMemoryHandler implements CommandExecutedListener
	{
		public void commandExecuted(CommandExecutedEvent event)
		{
			warnOnceIfLowOnMemory();	
		}
	}
	
	public boolean mainLinkFunction(String linkDescription)
	{	
		if (linkDescription.startsWith("Definition:"))
		{
			Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
			HtmlViewPanel htmlViewPanel = HtmlViewPanelWithMargins.createFromTextString(this, def.term, def.getDefintion());
			htmlViewPanel.showAsOkDialog();
		} 
		else if (isBrowserProtocol(linkDescription))
		{
	        launchBrowser(linkDescription);
		}
		else
			return false;
		
        return true;
	}
	
	private void launchBrowser(String linkDescription)
	{
		try 
		{
		    BrowserLauncherRunner runner = new BrowserLauncherRunner(
		    		new BrowserLauncher(null),
		            "",
		            linkDescription,
		            null);
		    new Thread(runner).start();
		}
		catch (Exception e) 
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to launch external web browser"));
		}
	}

	private boolean isBrowserProtocol(String linkDescription)
	{
		return linkDescription.startsWith(HTTP_PROTOCOL) || linkDescription.startsWith(MAIL_PROTOCOL);
	}
	

	private static String HTTP_PROTOCOL = "http";
	private static String MAIL_PROTOCOL = "mailto:";
	
	private static final String APP_PREFERENCES_FILENAME = "settings";
	private static final int TOOP_TIP_DELAY_MILLIS = 1000;
	
	public static final String DISABLED_CONPRO_IMPORT_EXPORT_MESSAGE = EAM.text("<HTML>Data exchange between Miradi and ConPro is not available in this version of Miradi. <BR> It is currently being tested, and should be available in the next version of Miradi. <BR>If you have questions, contact support@miradi.org.</HTML>");

	private ProjectServer database;
	private Actions actions;
	private AppPreferences preferences;
	private Project project;
	private HyperlinkHandler hyperlinkHandler;
	
	private NoProjectView noProjectView;
	private UmbrellaView summaryView;
	private DiagramView diagramView;
	private ThreatMatrixView threatMatrixView;
	private MapView mapView;
	private ScheduleView calendarView;
	private LibraryView libraryView;
	private TargetViabilityView targetViabilityView;
	private PlanningView planningView;
	private WorkPlanView workPlanView;
	private ReportsView reportView;
	
	private UmbrellaView currentView;
	private JPanel viewHolder;
	private ToolBarContainer toolBarBox;
	private MainMenuBar mainMenuBar;
	private MainStatusBar mainStatusBar;
	
	private JSplitPane spliterPane;
	private WizardManager wizardManager;
	private WizardPanel wizardPanel;
	
	private int existingCommandListenerCount;
	private int preventActionUpdatesCount;
	
	private boolean hasMemoryWarningBeenShown;
	private HumanWelfareTargetModeChangeHandler humanWelfareModeHandler;
	private WarnLowMemoryHandler lowMemoryHandler;
	private RefreshWizardHandler refreshWizardHandler;
	
}
