/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

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
import org.miradi.actions.ActionAbout;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
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
import org.miradi.objecthelpers.TwoLevelFileLoader;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.project.ProjectRepairer;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.resources.ResourcesHandler;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.DefaultHyperlinkHandler;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.SplitterPositionSaverAndGetter;
import org.miradi.views.Doer;
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
		actions = new Actions(this);
		hyperlinkHandler = new DefaultHyperlinkHandler(this, ResourcesHandler.class);
	}
	
	public void start(String[] args) throws Exception
	{
		if(Arrays.asList(args).contains("--demo"))
			demoMode = true;
		
		File appPreferencesFile = getPreferencesFile();
		preferences.load(appPreferencesFile);
		project.addCommandExecutedListener(this);
		
		ToolTipManager.sharedInstance().setInitialDelay(TOOP_TIP_DELAY_MILLIS);
		ToolTipManager.sharedInstance().setReshowDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		
		loadCustomAppColors();
		setIconImage(new MiradiResourceImageIcon("images/appIcon.png").getImage());
		
		WizardTitlePanel wizardTitlePanel = new WizardTitlePanel(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		mainStatusBar = new MainStatusBar();
		updateTitle();
		setSize(new Dimension(900, 700));
		setJMenuBar(mainMenuBar);
		getContentPane().setLayout(new BorderLayout());

		addWindowListener(new WindowEventHandler());

		wizardPanel = createWizardPanel(wizardTitlePanel);
		
		noProjectView = new NoProjectView(this);
		summaryView = new SummaryView(this);
		diagramView = new DiagramView(this);
		threatMatrixView = new ThreatMatrixView(this);
//		budgetView = new BudgetView(this);
//		workPlanView = new WorkPlanView(this);
		mapView = new MapView(this);
		calendarView = new ScheduleView(this);
		libraryView = new LibraryView(this);
//		strategicPlanView = new StrategicPlanView(this);
//		monitoringView = new MonitoringView(this);
		targetViabilityView = new TargetViabilityView(this);
		planningView = new PlanningView(this);
		reportView = new ReportsView(this);

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(summaryView, summaryView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(threatMatrixView, threatMatrixView.cardName());
//		viewHolder.add(budgetView, budgetView.cardName());
//		viewHolder.add(workPlanView, workPlanView.cardName());
		viewHolder.add(mapView, mapView.cardName());
		viewHolder.add(calendarView, calendarView.cardName());
		viewHolder.add(libraryView, libraryView.cardName());
//		viewHolder.add(strategicPlanView, strategicPlanView.cardName());
//		viewHolder.add(monitoringView, monitoringView.cardName());
		viewHolder.add(targetViabilityView, targetViabilityView.cardName());
		viewHolder.add(planningView, planningView.cardName());
		viewHolder.add(reportView, reportView.cardName());

		getWizardManager().setOverViewStep(NoProjectView.getViewName());
		updateActionStates();

		displayExpirationNoticeIfAppropriate();
		
		setExtendedState(ICONIFIED);
		setSize(0,0);
		setLocation(-100, -100);
		setVisible(true);
		if(!Arrays.asList(args).contains("--nosplash"))
		{
			Doer aboutDoer = diagramView.getDoer(ActionAbout.class);
			aboutDoer.setMainWindow(this);
			aboutDoer.doIt();
		}

		setSize(preferences.getMainWindowWidth(), preferences.getMainWindowHeight());
		setLocation(preferences.getMainWindowXPosition(), preferences.getMainWindowYPosition());
		setExtendedState(NORMAL);
		if(preferences.getIsMaximized())
		{
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}
	}

	private void loadCustomAppColors() throws Exception
	{
		try
		{
			File home = EAM.getHomeDirectory();
			FileInputStream is = new FileInputStream(new File(home, TwoLevelFileLoader.COLORS_FILE));
			ColorsFileLoader colorsLoader = new ColorsFileLoader();
			TwoLevelEntry[] colors = colorsLoader.load(is);
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
		
	private void displayExpirationNoticeIfAppropriate()
	{
		MultiCalendar now = new MultiCalendar();
		MultiCalendar expiresOn = MultiCalendar.createFromIsoDateString("2008-04-01");
		if(now.before(expiresOn))
			return;
		
		EAM.notifyDialog("<html>" +
				"<b>This is an expired beta test version of Miradi.</b> <br>" +
				"<br>" +
				"Although this copy of the software will continue to function indefinitely, <br>" +
				"it wasn't designed to be a production piece of software: <br>" +
				"you're missing out on many bug fixes and improvements. <br>" +
				"<br>" +
				"Please log onto www.Miradi.org and obtain a licensed version. <br>" +
				"In addition to getting the latest functionality and features, <br>" +
				"you will also support your fair share of Miradi's ongoing development. <br>" +
				"Miradi is a strictly not-for-profit project. All money raised from <br>" +
				"distribution and licensing is used to fund support for users and to improve Miradi.");
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
	
	public DiagramComponent getDiagramComponent()
	{
		if(diagramView == null)
			return null;
		return diagramView.getDiagramComponent();
	}
	
	public Actions getActions()
	{
		return actions;
	}
	
	public void createOrOpenProject(File projectDirectory)
	{
		preventActionUpdates();
		try
		{
			project.createOrOpen(projectDirectory);
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
		getWizard().setContents(step);
		getWizard().refresh();
		validate();
	}
	
	private void setViewForStep(SkeletonWizardStep step) throws Exception
	{
		if(getCurrentView() != null && step.getViewName().equals(getCurrentView().cardName()))
			return;
		
		setCurrentView(step.getViewName());
	}

	public void updateStatusBar()
	{
		setDiagramViewStatusBar();
	}

	private void setDiagramViewStatusBar()
	{
		if (!diagramView.getViewName().equals(currentView.getName()))
			return;
		
		if(getProject().getLayerManager().areAllNodesVisible())
			mainStatusBar.setStatusAllLayersVisible();
		else
			mainStatusBar.setStatusHiddenLayers();
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
		if(viewName.equals(summaryView.cardName()))
			setCurrentView(summaryView);
		else if(viewName.equals(diagramView.cardName()))
			setCurrentView(diagramView);
		else if(viewName.equals(noProjectView.cardName()))
			setCurrentView(noProjectView);
		else if(viewName.equals(threatMatrixView.cardName()))
			setCurrentView(threatMatrixView);
//		else if(viewName.equals(budgetView.cardName()))
//			setCurrentView(budgetView);
//		else if(viewName.equals(workPlanView.cardName()))
//			setCurrentView(workPlanView);
		else if(viewName.equals(mapView.cardName()))
			setCurrentView(mapView);
		else if(viewName.equals(calendarView.cardName()))
			setCurrentView(calendarView);
		else if(viewName.equals(libraryView.cardName()))
			setCurrentView(libraryView);
//		else if(viewName.equals(strategicPlanView.cardName()))
//			setCurrentView(strategicPlanView);
//		else if(viewName.equals(monitoringView.cardName()))
//			setCurrentView(monitoringView);
		else if (viewName.equals(targetViabilityView.cardName()))
			setCurrentView(targetViabilityView);
		else if(viewName.equals(planningView.cardName()))
			setCurrentView(planningView);
		else if (viewName.equals(reportView.cardName()))
			setCurrentView(reportView);
		
		else
		{
			EAM.logError("MainWindow.switchToView: Unknown view: " + viewName);
			setCurrentView(summaryView);
		}
	}
	
	public static boolean isDemoMode()
	{
		return demoMode;
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
		if (getDiagramComponent()!=null)
			getDiagramComponent().updateDiagramComponent();
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
			HtmlViewPanel htmlViewPanel = new HtmlViewPanel(this, def.term,  def.getDefintion());
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

	private static boolean demoMode;

	protected Actions actions;
	private AppPreferences preferences;
	private Project project;
	private HyperlinkHandler hyperlinkHandler;
	
	private NoProjectView noProjectView;
	private UmbrellaView summaryView;
	private DiagramView diagramView;
	private ThreatMatrixView threatMatrixView;
//	private BudgetView budgetView;
//	private WorkPlanView workPlanView;
	private MapView mapView;
	private ScheduleView calendarView;
	private LibraryView libraryView;
//	private StrategicPlanView strategicPlanView;
//	private TabbedView monitoringView;
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
