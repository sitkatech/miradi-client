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
package org.miradi.main;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;
import org.martus.swing.HyperlinkHandler;
import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeWriter;
import org.miradi.actions.Actions;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogs.ModalProjectOverwriteDialog;
import org.miradi.dialogs.ModalProjectRenameDialog;
import org.miradi.dialogs.ProjectCorruptionDialog;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.dialogs.notify.NotifyDialog;
import org.miradi.dialogs.notify.NotifyDialogTemplateFactory;
import org.miradi.dialogs.planning.AbstractWorkPlanRowColumnProvider;
import org.miradi.exceptions.*;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.legacyprojects.LegacyProjectUtilities;
import org.miradi.main.menu.MainMenuBar;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.objecthelpers.*;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.project.*;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.TableRowHeightModeQuestion;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.utils.*;
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
import org.miradi.wizard.noproject.WelcomeCreateStep;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.List;

public class MainWindow extends JFrame implements ClipboardOwner, SplitterPositionSaverAndGetter
{
	public static MainWindow create() throws Exception
	{
		return new MainWindow();
	}
	
	public MainWindow() throws Exception
	{
		this(new Project());
	}

	public MainWindow(Project projectToUse) throws Exception
	{
		preferences = new AppPreferences();
		project = projectToUse;
		setFocusCycleRoot(true);
		wizardManager = new WizardManager(this);
		hyperlinkHandler = new DefaultHyperlinkHandler(this);
		setLanguage(null);

		projectSaver = new AutomaticProjectSaver(project);
	}
	
	public void start(String[] args) throws Exception
	{
		createSessionFile();
		Vector<String> commandLineArgumentsToUse = new Vector<String>(Arrays.asList(args));
		setCommandLineArguments(commandLineArgumentsToUse);
		
		if(ResourcesHandler.isRunningFromInsideJar())
		{
			if(!VersionConstants.hasValidVersion())
				EAM.logWarning("Invalid or missing Miradi version number");
			if(!VersionConstants.hasValidTimestamp())
				EAM.logWarning("Invalid or missing Miradi build identifier");
		}
		
		File appPreferencesFile = getPreferencesFile();
		preferences.load(appPreferencesFile);

		Miradi.setBestLookAndFeel(preferences.getLookAndFeelThemeName());

		ensureFontSizeIsSet();
		
		humanWelfareModeHandler = new HumanWelfareTargetModeChangeHandler();
		biophysicalFactorModeChangeHandler = new BiophysicalFactorModeChangeHandler();
		lowMemoryHandler = new WarnLowMemoryHandler();
		refreshWizardHandler = new RefreshWizardHandler();
		
		getProject().addCommandExecutedListener(humanWelfareModeHandler);
		getProject().addCommandExecutedListener(biophysicalFactorModeChangeHandler);
		getProject().addCommandExecutedListener(refreshWizardHandler);
		getProject().addCommandExecutedListener(lowMemoryHandler);
		
		ToolTipManager.sharedInstance().setInitialDelay(TOOLTIP_DELAY_MILLIS);
		ToolTipManager.sharedInstance().setReshowDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

		loadCustomAppColors();
		addWindowListener(new WindowEventHandler());
		setSize(new Dimension(900, 700));

		setExtendedState(ICONIFIED);
		setSize(0,0);
		setLocation(-100, -100);
		setVisible(true);

		File poFileIfAny = Miradi.getPoFileIfAny();
		if(poFileIfAny != null)
			setLanguageFromPoFile(poFileIfAny);
		else
			setLanguage(getAppPreferences().getLanguageCode());

		new SampleInstaller(getAppPreferences()).installSampleProjects(new NullProgressMeter());

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
		setLocation(getLocationFromPreferences());
		setExtendedState(NORMAL);
		if(preferences.getIsMaximized() && !Miradi.isLinux())
		{
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
		
        File preferredHomeDir = EAM.getPreferredHomeDirectory();
        if (!EAM.isPreferredHomeDirectoryValid(preferredHomeDir))
        {
            EAM.setDefaultHomeDirectoryPreference();
            if (preferredHomeDir != null)
                NotifyDialog.notify(this, NotifyDialogTemplateFactory.notifyUserOfInvalidPreferredHomeDirectory(preferredHomeDir));
        }

		safelySavePreferences();
	}
	
	private void createSessionFile() throws Exception
	{
        File homeDirectory = EAM.getHomeDirectory();
        homeDirectory.mkdirs();
        File sessionFile = new File(homeDirectory, "session.log");
		FileUtilities.deleteIfExistsWithRetries(sessionFile);
		StringBuffer buffer = new StringBuffer();
		buffer.append(MiradiLogger.createLogHeader());
		UnicodeWriter fileWriter = new UnicodeWriter(sessionFile);
		fileWriter.write(buffer.toString());
		fileWriter.close();
	}

	private void setCommandLineArguments(Vector<String> commandLineArgumentsToUse)
	{
		commandLineArguments = commandLineArgumentsToUse;
	}

	public AutomaticProjectSaver getProjectSaver()
	{
		return projectSaver;
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

	private Point getLocationFromPreferences()
	{
		int left = preferences.getMainWindowXPosition();
		int top = preferences.getMainWindowYPosition();
		return new Point(left, top);
	}

	private void ensureFontSizeIsSet()
	{
		if(preferences.getPanelFontSize() == 0)
			setDataPanelFontSize(getSystemFontSize());
	}

	public static int getSystemFontSize()
	{
		return new JLabel().getFont().getSize();
	}
	
	private void setLanguage(String languageCode) throws Exception
	{
		Miradi.switchToLanguage(languageCode);
		actions = new Actions(this);
	}

	private void setLanguageFromPoFile(File poFileIfAny) throws Exception
	{
		Miradi.switchToLanguage(poFileIfAny);
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
		if (splitterPane !=null)
		{
			splitterPane = null;
		}
		
		getContentPane().add(wizardPanel, BorderLayout.CENTER);
	}
	
	public void showDivider()
	{
		getContentPane().removeAll();
		
		splitterPane = new ViewSplitPane(this, wizardPanel, viewHolder);

		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(splitterPane, BorderLayout.CENTER);
		getContentPane().add(getMainStatusBar(), BorderLayout.AFTER_LAST_LINE);
	}
	
	public void forceViewSplitterToMiddle()
	{
		if (splitterPane == null)
			return;
		splitterPane.setDividerLocation(splitterPane.getHeight()/2);
	}
	
	public int getDividerLocation()
	{
		if (splitterPane !=null)
			return splitterPane.getDividerLocation();
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
	
	static class AlreadyHandledException extends Exception
	{
	}

	public void createOrOpenProject(File projectFile)
	{
		preventActionUpdates();
		project.beginCommandSideEffectMode();
		try
		{
			if (!canCreateOrOpenProject(projectFile))
			{
				EAM.errorDialog(EAM.text("A project with this name already exists."));
				return;
			}

			final boolean projectIsPreparedToBeOpened = prepareProjectToOpen(projectFile);
			if (!projectIsPreparedToBeOpened)
				return;
			
			createOrOpenProjectInBackground(projectFile);
			projectSaver.startSaving(projectFile);
			
			repairProject();

			refreshWizard();

			validate();
			updateTitle();
			updateStatusBar();
			getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
		}
		catch(UserCanceledException e)
		{
			EAM.notifyDialog(EAM.text("Action canceled"));
			try
			{
				closeProject(false);
			}
			catch(Exception exceptionDuringClose)
			{
				throw new RuntimeException("Unable to close the partially-opened project", exceptionDuringClose);
			}
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
		catch(FileLocker.AlreadyLockedException e)
		{
			EAM.errorDialog(EAM.text("That project is in use by another copy of this application"));
		}
		catch(NotMiradiProjectFileException e)
		{
			EAM.errorDialog(EAM.text("That file is not a valid Miradi project"));
		}
		catch(ProjectFileTooOldException e)
		{
			EAM.errorDialog(EAM.text("That project is too old to be opened with this version of Miradi"));
		}
		catch(ProjectFileTooNewException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("That project cannot be opened because it was created by a newer version of Miradi"));
		}
		catch(FutureSchemaVersionException e)
		{
			EAM.errorDialog(EAM.text("That project cannot be opened because it was created by a newer version of Miradi"));
		}
		catch(OldSchemaVersionException e)
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

	private boolean prepareProjectToOpen(File projectFile) throws Exception
	{
		if (projectFile.exists())
		{		
			validateProjectVersion(projectFile);
			MigrationManager migrationManager = new MigrationManager();
			if (migrationManager.needsMigration(projectFile))
			{
				final String[] labels = new String[]{ConstantButtonNames.MIGRATE, ConstantButtonNames.CANCEL};
				final String message = EAM.text("This project was saved using an older version of Miradi. Before it can be opened, it needs to be migrated \n" +
												"to the current data format. This is an automatic process, and a backup copy will be saved before the migrated \n" +
												"version is written. After the project has been migrated, this project file will no longer be readable by \n" +
												"older versions of Miradi." +
												"\n\nDo you wish to proceed with the migration?");
				final int result = EAM.confirmDialog(EAM.text("Migration"), message, labels);
				if (result != 0)
					return false;

				return possiblyMigrateWithBackup(projectFile, migrationManager);
			}
		}

		return true;
	}

	private boolean possiblyMigrateWithBackup(File projectFile, MigrationManager migrationManager) throws Exception, IOException
	{
		RawProject rawProjectToMigrate = RawProjectLoader.loadProject(projectFile);
		MigrationResult migrationResult = migrationManager.migrate(rawProjectToMigrate, Project.getMiradiVersionRange());
		if (migrationResult.didLoseData())
		{
			final String[] labels = new String[]{ConstantButtonNames.MIGRATE, ConstantButtonNames.CANCEL};
			final String message = EAM.substituteSingleString(EAM.text("If you continue with this migration, some data will be lost.\n\n" +
											"Would you like to continue anyway?\n\n" +
											"Summary of data losses:\n\n" +
											 "%s \n"), migrationResult.getUserFriendlyGroupedDataLossMessagesAsString());
			final int result = EAM.confirmDialogWithScrollPanel(EAM.text("Migration"), message, labels);
			if (result != 0)
				return false;
		}
 		if (migrationResult.didSucceed())
		{
			MigrationManager.createBackup(projectFile);
			String migratedProjectAsString = RawProjectSaver.saveProject(rawProjectToMigrate);
			UnicodeWriter fileWriter = new UnicodeWriter(projectFile);
			fileWriter.write(migratedProjectAsString);
			fileWriter.close();
			return true;
		}
		
		return false;
	}

	private boolean canCreateOrOpenProject(File projectFile)
	{
		if (projectFile.exists())
			return true;
		
		File newProjectFile = AutomaticProjectSaver.getNewFile(projectFile);
		if (newProjectFile.exists())
			return false;
		
		final File oldProjectFile = AutomaticProjectSaver.getOldFile(projectFile);
		return !oldProjectFile.exists();
	}
	
	private void validateProjectVersion(File projectFile) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		migrationManager.validateProjectVersion(projectFile);
	}

	private void createOrOpenProjectInBackground(File projectFile) throws Exception
	{
		String title = EAM.text("Create Project");
		if(projectFile.exists())
			title = EAM.text("Open Project");
		ProgressDialog progressDialog = new ProgressDialog(this, title);
		ProjectOpenWorker worker = new ProjectOpenWorker(progressDialog, project, projectFile);
		progressDialog.doWorkInBackgroundWhileShowingProgress(worker);
	}
	
	private static class ProjectOpenWorker extends MiradiBackgroundWorkerThread
	{
		public ProjectOpenWorker(ProgressInterface progressInterfaceToUse, Project projectToUse, File projectFileToUse)
		{
			super(progressInterfaceToUse);
			
			project = projectToUse;
			projectFile = projectFileToUse;
		}
		
		@Override
		protected void doRealWork() throws Exception
		{
			ProgressInterface progressMeter = getProgressIndicator();

			if(projectFile.exists())
			{
				progressMeter.setStatusMessage(EAM.text("Opening project file..."), 3);
				progressMeter.incrementProgress();
				String contents = UnicodeReader.getFileContents(projectFile);
				progressMeter.incrementProgress();
				ProjectLoader.loadProject(new UnicodeStringReader(contents), project);
				project.finishOpeningAfterLoad(projectFile);
				progressMeter.incrementProgress();
			}
			else
			{
				project.createWithDefaultObjectsAndDiagramHelp(projectFile, progressMeter);
			}
		}

		private Project project;
		private File projectFile;
	}

	private void repairProject() throws Exception
	{
		final String beforeRepairSnapShot = ProjectSaver.createSnapShot(getProject());
		ProjectRepairer repairer = new ProjectRepairer(project);
		if (commandLineArguments.contains(Miradi.REPAIR_PROJECT_ON_SWITCH))
			repairMissingObjects(repairer);
		
		quarantineOrphans(repairer);
		repairer.repairProblemsWherePossible();
		scanForSeriousCorruption(repairer);
		final String afterRepairSnapShot = ProjectSaver.createSnapShot(getProject());
		if (!beforeRepairSnapShot.equals(afterRepairSnapShot))
		{
			projectSaver.safeSave();
			showRepairMessagesAsUserDialog(repairer.getMessages());
			EAM.logDebug("Project was repaired and saved!");
		}
	}

	private void showRepairMessagesAsUserDialog(TreeSet<String> messages)
	{
		StringBuffer appendedMessages = new StringBuffer();
		for (String message : messages)
		{
			appendedMessages.append(message);
			appendedMessages.append(StringUtilities.NEW_LINE);
		}
		
		if (appendedMessages.length() > 0)
		{
			EAM.notifyDialog(appendedMessages.toString());
		}
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

		Vector<String> problems = new Vector<String>();
		for(ORef missingRef : rawProblems.keySet())
		{
			ORefSet referrers = rawProblems.get(missingRef);
			String typeName = ObjectType.getUserFriendlyObjectTypeName(getProject(), missingRef.getObjectType());
			problems.add("Missing " + typeName + " " + missingRef + " referred to by " + referrers.toString());
		}
		Collections.sort(problems);
		
		String listOfProblems = "";
		for(String problem : problems)
		{
			listOfProblems += problem + "\n";
		}
		
		if(!ProjectCorruptionDialog.askUserWhetherToOpen(this, title, bodyText, listOfProblems))
			throw new AlreadyHandledException();
	}

	private void repairMissingObjects(ProjectRepairer repairer) throws Exception
	{
		HashMap<ORef, ORefSet> rawProblems = repairer.getListOfMissingObjects();
		repairMissingObjects(rawProblems.keySet());
	}

	private void repairMissingObjects(Set<ORef> missingObjectRefs) throws Exception
	{
		
		for(ORef missingObjectRef : missingObjectRefs)
		{
			getProject().createObject(missingObjectRef);
			EAM.logWarning("Missing object recreated:" + missingObjectRef);
		}
	}

	public SummaryView getSummaryView()
	{
		return summaryView;
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
		closeProject(true);
	}
	
	private void closeProject(boolean setOverViewStep) throws Exception
	{
		projectSaver.stopSaving();
		project.close();
		EAM.logDebug(getMemoryStatistics());
		if (setOverViewStep)
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
			getProject().removeCommandExecutedListener(biophysicalFactorModeChangeHandler);
			getProject().removeCommandExecutedListener(lowMemoryHandler);
			getProject().removeCommandExecutedListener(refreshWizardHandler);
			projectSaver.dispose();
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
				getMainStatusBar().setWarningStatus(EAM.text("Project People Filter Is On"));
			else if (hasNonMatchingFiscalYearStartMonth(getProject()))
				getMainStatusBar().setWarningStatus(EAM.text("Existing data for a different fiscal year is being excluded"));
			else if (isDataOutsideOfCurrentProjectDateRange())
				getMainStatusBar().setWarningStatus(("* Assignments, work units and expenses outside the work plan begin/end dates are not shown"));
			else
				clearStatusBar();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			clearStatusBar();
		}
	}
	
	public boolean areAnyProjectResourceFiltersOn() throws Exception
	{
		ORefList refs = getProjectResourceFilterRefs();

		return refs.size() > 0;
	}

	public ORefList getProjectResourceFilterRefs() throws Exception
	{
		TableSettings tableSettings = AbstractWorkPlanRowColumnProvider.getWorkPlanTableSettings(getProject());
		CodeToCodeListMap tableSettingsMap = tableSettings.getTableSettingsMap();
		return tableSettingsMap.getRefList(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);
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
		assignmentRefs.addAll(projectToUse.getPool(ExpenseAssignmentSchema.getObjectType()).getRefList());
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
	
	public MainStatusBar getMainStatusBar()
	{
		return mainStatusBar;
	}

	private boolean isDataOutsideOfCurrentProjectDateRange()
	{
		String startDate = getProject().getProjectCalendar().getPlanningStartDate();
		String endDate = getProject().getProjectCalendar().getPlanningEndDate();

		if (startDate.trim().length() <= 0 || endDate.trim().length() <= 0)
			return false;
		
		MultiCalendar multiStartDate = MultiCalendar.createFromIsoDateString(startDate);
		MultiCalendar multiEndDate = MultiCalendar.createFromIsoDateString(endDate);
		
		if (multiStartDate.after(multiEndDate))
			return false;

		return SummaryPlanningWorkPlanSubPanel.hasAssignedDataOutsideOfProjectDateRange(getProject());
	}

	private void updateAfterCommand(CommandExecutedEvent event)
	{
		try
		{
			if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME))
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
			preferences.setMainWindowHeight(getHeight());
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
			return getSystemFontSize();
		
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

	public void setColorScheme(String colorScheme)
	{
		preferences.setColorScheme(colorScheme);
	}

	public String getColorScheme()
	{
		return preferences.getColorScheme();
	}

	public void setLookAndFeelThemeName(String lookAndFeelTheme)
	{
		preferences.setLookAndFeelThemeName(lookAndFeelTheme);
	}

	public String getLookAndFeelThemeName()
	{
		return preferences.getLookAndFeelThemeName();
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
			if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE))
				updateMenuOptions();
		}
	}
	
	private class BiophysicalFactorModeChangeHandler implements CommandExecutedListener
	{
		public void commandExecuted(CommandExecutedEvent event)
		{
			if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_FACTOR_MODE))
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
			if (Desktop.isDesktopSupported())
			{
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(new URI(linkDescription));
			}
			else
			{
				BrowserLauncherRunner runner = new BrowserLauncherRunner(
						new BrowserLauncher(null),
						"",
						linkDescription,
						null);
				new Thread(runner).start();
			}
		}
		catch (Exception e) 
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to launch external web browser"));
		}
	}

	private boolean isBrowserProtocol(String linkDescription)
	{
		return linkDescription.startsWith(HTTP_PROTOCOL) || linkDescription.startsWith(HTTPS_PROTOCOL) || linkDescription.startsWith(MAIL_PROTOCOL);
	}

    public String getDestinationProjectFileName(File proposedProjectFile) throws Exception
    {
		String proposedProjectName = FileUtilities.fileNameWithoutExtension(proposedProjectFile.getName());
		String proposedProjectFileName = AbstractMpfFileFilter.createNameWithExtension(proposedProjectName);
		if (!Project.isValidProjectName(proposedProjectName))
		{
			String userEnteredProjectName = askUserForProjectName(proposedProjectName);
			if (userEnteredProjectName == null)
			{
				return null;
			}
			proposedProjectName = userEnteredProjectName;
			proposedProjectFileName = AbstractMpfFileFilter.createNameWithExtension(userEnteredProjectName);
		}

		proposedProjectFile = new File(EAM.getHomeDirectory(), proposedProjectFileName);
		if (projectExists(proposedProjectFile))
		{
			String overwrittenProjectFileName = ModalProjectOverwriteDialog.showDialog(this, EAM.text("Save As..."), proposedProjectName);
			if (overwrittenProjectFileName == null)
				return null;

			proposedProjectFileName = AbstractMpfFileFilter.createNameWithExtension(overwrittenProjectFileName);
			FileUtilities.createMpfBackup(proposedProjectFile, EAM.substituteSingleString(EAM.text("(%s)"), "Overridden-backup"));
		}

		return proposedProjectFileName;
    }

	public static boolean projectExists(File file) throws Exception
	{
		if(LegacyProjectUtilities.isExistingLocalProject(file))
			return true;
		
		if(file.exists())
			return true;
		
		return false;
	}
	
	private String askUserForProjectName(String projectName) throws Exception
	{
		String legalProjectName = Project.makeProjectNameLegal(projectName);
		return ModalProjectRenameDialog.showDialog(this, PROJECT_NAME_TEXT, legalProjectName);
	}

	private static final String PROJECT_NAME_TEXT = "<html>" +
			EAM.text("<div>The selected project's name contains invalid characters.</div>") +
			EAM.text("<div>The below alternative name is valid. Edit this name as desired.</div>") +
			"<br>&nbsp;&nbsp;&nbsp;<i>" + WelcomeCreateStep.getLegalProjectNameNote();

	private static String HTTP_PROTOCOL = "http";
	private static String HTTPS_PROTOCOL = "https";
	private static String MAIL_PROTOCOL = "mailto:";
	
	private static final String APP_PREFERENCES_FILENAME = "settings";
	private static final int TOOLTIP_DELAY_MILLIS = 1000;
	
	private Actions actions;
	private AppPreferences preferences;
	private Project project;
	private HyperlinkHandler hyperlinkHandler;
	private AutomaticProjectSaver projectSaver;
	
	private NoProjectView noProjectView;
	private SummaryView summaryView;
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
	
	private JSplitPane splitterPane;
	private WizardManager wizardManager;
	private WizardPanel wizardPanel;
	
	private int existingCommandListenerCount;
	private int preventActionUpdatesCount;
	
	private boolean hasMemoryWarningBeenShown;
	private HumanWelfareTargetModeChangeHandler humanWelfareModeHandler;
	private BiophysicalFactorModeChangeHandler biophysicalFactorModeChangeHandler;
	private WarnLowMemoryHandler lowMemoryHandler;
	private RefreshWizardHandler refreshWizardHandler;
	private List<String> commandLineArguments;	
}
