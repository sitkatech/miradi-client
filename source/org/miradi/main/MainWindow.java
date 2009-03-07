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
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.martus.swing.HyperlinkHandler;
import org.martus.util.DirectoryLock;
import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeReader;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.exceptions.FutureVersionException;
import org.miradi.exceptions.InvalidDateRangeException;
import org.miradi.exceptions.OldVersionException;
import org.miradi.exceptions.UnknownCommandException;
import org.miradi.ids.BaseId;
import org.miradi.main.menu.MainMenuBar;
import org.miradi.objecthelpers.ColorsFileLoader;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TwoLevelEntry;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.project.ProjectRepairer;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.TableRowHeightModeQuestion;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.DefaultHyperlinkHandler;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.SplitterPositionSaverAndGetter;
import org.miradi.utils.Translation;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.library.LibraryView;
import org.miradi.views.map.MapView;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.reports.ReportsView;
import org.miradi.views.schedule.ScheduleView;
import org.miradi.views.summary.SummaryView;
import org.miradi.views.targetviability.TargetViabilityView;
import org.miradi.views.threatmatrix.ThreatMatrixView;
import org.miradi.views.umbrella.Definition;
import org.miradi.views.umbrella.DefinitionCommonTerms;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.ViewSplitPane;
import org.miradi.wizard.SkeletonWizardStep;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WizardTitlePanel;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

public class MainWindow extends JFrame implements CommandExecutedListener, ClipboardOwner, SplitterPositionSaverAndGetter
{
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
	}
	
	public void start(String[] args) throws Exception
	{
		if(!EAM.initializeHomeDirectory())
			System.exit(1);

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
		project.addCommandExecutedListener(this);
		
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
			new SampleInstaller(getAppPreferences()).installSampleProjects();
		}

		if(hasExpired() || commandLineArguments.contains("--expired"))
		{
			displayExpirationNotice();
		}
		
		setIconImage(new MiradiResourceImageIcon("images/appIcon.png").getImage());

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
		viewHolder.add(reportView, reportView.cardName());

		getWizardManager().setOverViewStep(NoProjectView.getViewName());
		updateActionStates();

		setSize(preferences.getMainWindowWidth(), preferences.getMainWindowHeight());
		setLocation(preferences.getMainWindowXPosition(), preferences.getMainWindowYPosition());
		setExtendedState(NORMAL);
		if(preferences.getIsMaximized())
		{
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
		
		savePreferences();
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
					getAppPreferences().setWizardTitleBackground(colorEntry.getEntryDescription());

				if (colorEntry.getEntryCode().equals("WizardPanel"))
					getAppPreferences().setWizardBackgroundColor(colorEntry.getEntryDescription());

				if (colorEntry.getEntryCode().equals("WizardSidebar"))
					getAppPreferences().setWizardSidebarBackgroundColor(colorEntry.getEntryDescription());

				if (colorEntry.getEntryCode().equals("DataBorder"))
					getAppPreferences().setDarkControlPanelBackgroundColor(colorEntry.getEntryDescription());

				if (colorEntry.getEntryCode().equals("DataPanel"))
					getAppPreferences().setDataPanelBackgroundColor(colorEntry.getEntryDescription());

				if (colorEntry.getEntryCode().equals("ControlBar"))
					getAppPreferences().setControlPanelBackgroundColor(colorEntry.getEntryDescription());
			}
			
			EAM.logVerbose("Loaded colors from file");
		}
		catch (IOException e)
		{
			EAM.logVerbose("No color file found");
		}
	}
		
	private void displayExpirationNotice() throws Exception
	{
		String html = Translation.getHtmlContent("ExpiredWarning.html");
		HtmlViewPanelWithMargins viewer = HtmlViewPanelWithMargins.createFromTextString(this, EAM.text("Information"), html);
		viewer.showAsOkDialog();
	}

	private boolean hasExpired()
	{
		MultiCalendar now = new MultiCalendar();
		final String isoExpiration = "2009-09-01";
		EAM.logVerbose("Expires on: " + isoExpiration);
		MultiCalendar expiresOn = MultiCalendar.createFromIsoDateString(isoExpiration);
		boolean hasExpired = now.after(expiresOn);
		return hasExpired;
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
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);
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
			if(getProject().getCommandListenerCount() != existingCommandListenerCount)
			{
				EAM.logError("CommandListener orphaned by " + getClass());
				getProject().logCommandListeners(System.err);
			}
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
			updateToolBar();
		}
		finally
		{
			allowActionUpdates();
			mainMenuBar.updateMenuOptions();
			updateActionsAndStatusBar();
		}
	}

	public void updateToolBar()
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
			JToolBar toolBar = view.createToolBar();
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
	
	public void createOrOpenProject(File projectDirectory)
	{
		preventActionUpdates();
		project.beginCommandSideEffectMode();
		try
		{
			int projectAction = project.createOrOpen(projectDirectory);
			if (projectAction == Project.PROJECT_WAS_CREATED)
				project.createDefaultHelpTextBoxDiagramFactor();
			
			ProjectRepairer.repairAnyProblems(project);
			ProjectRepairer.scanForCorruptedObjects(project);
			refreshWizard();

			validate();
			updateTitle();
			updateStatusBar();
			getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
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
		mainStatusBar.setStatus("");
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

	public void exitNormally()
	{
		try
		{
			closeProject();
			savePreferences();
			System.exit(0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			System.exit(1);
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		updateAfterCommand(event);
		warnOnceIfLowOnMemory();
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

	public void setStatusBarIfDataExistsOutOfRange()
	{
		try
		{
			final String dataOutOfRange = EAM.text("WorkPlan/Financial data outside project begin/end dates will not be shown");
			if (isDataOutsideOfcurrentProjectDateRange())
				mainStatusBar.setStatus(dataOutOfRange);
			else
				mainStatusBar.setStatusReady();
		}
		catch (InvalidDateRangeException e)
		{
			mainStatusBar.setStatus(e.getMessage());
			EAM.logError(e.getMessage());
		}
	}

	//TODO refactor this method (nested for loops)
	private boolean isDataOutsideOfcurrentProjectDateRange() throws InvalidDateRangeException
	{
		String startDate = getProject().getProjectCalendar().getPlanningStartDate();
		String endDate = getProject().getProjectCalendar().getPlanningEndDate();

		if (startDate.trim().length() <= 0 || endDate.trim().length() <= 0)
			return false;
		
		MultiCalendar multiStartDate = MultiCalendar.createFromIsoDateString(startDate);
		MultiCalendar multiEndDate = MultiCalendar.createFromIsoDateString(endDate);
		
		if (multiStartDate.after(multiEndDate))
			throw new InvalidDateRangeException(EAM.text("WARNING: Project end date before start date."));

		try
		{
			DateRange projectDateRange = new DateRange(multiStartDate, multiEndDate);
			
			BaseId[] assignmentIds = getProject().getAssignmentPool().getIds();
			for (int i = 0; i < assignmentIds.length; i++)
			{
				Assignment assignment = (Assignment) getProject().findObject(new ORef(ObjectType.ASSIGNMENT, assignmentIds[i]));
				DateRangeEffortList effortList = assignment.getDetails();
				for (int j = 0; j < effortList.size(); j++)
				{
					DateRangeEffort effort = effortList.get(j);
					DateRange effortDateRange = effort.getDateRange();
					if (!projectDateRange.contains(effortDateRange))
						return true;
				}
			}
			
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
		
		return false;
	}

	private void updateAfterCommand(CommandExecutedEvent event)
	{
		try
		{
			if(event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			{
				CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
				boolean isMetadataCommand = cmd.getObjectORef().equals(getProject().getMetadata().getRef());
				boolean isCurrentWizardScreenChange = cmd.getFieldTag().equals(ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME);
				if(isMetadataCommand && isCurrentWizardScreenChange)
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
		
		else if(viewName.equals(planningView.cardName()))
			return planningView;
		
		else if (viewName.equals(reportView.cardName()))
			return reportView;

		else
		{
			EAM.logError("MainWindow.switchToView: Unknown view: " + viewName);
			return summaryView;
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
		return new Font(fontFamily.getLabel(),Font.PLAIN, getDataPanelFontSizeWithDefault());
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
	
	public void setTaggedInt(String name, int value)
	{
		preferences.setTaggedInt(name, value);
	}
	
	public int getTaggedInt(String name)
	{
		return preferences.getTaggedInt(name);
	}
	
	public void setTaggedString(String name, String value)
	{
		preferences.setTaggedString(name, value);
	}
	
	public String getTaggedString(String name)
	{
		return preferences.getTaggedString(name);
	}
	
	public void saveDiagramZoomSetting(String name, double setting)
	{
		preferences.setTaggedDouble(name, setting);
	}
	
	public double getDiagramZoomSetting(String name)
	{
		return preferences.getTaggedDouble(name);
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
		public void windowClosing(WindowEvent event)
		{
			try
			{
				ObjectDataInputField.saveFocusedFieldPendingEdits();
				exitNormally();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				System.exit(1);
			}
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
	
	public static final boolean ALLOW_CONPRO_IMPORT_EXPORT = true;
	public static final String DISABLED_CONPRO_IMPORT_EXPORT_MESSAGE = EAM.text("<HTML>Data exchange between Miradi and ConPro is not available in this version of Miradi. <BR> It is currently being tested, and should be available in the next version of Miradi. <BR>If you have questions, contact support@miradi.org.</HTML>");

	protected Actions actions;
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

}
