/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.project.FutureVersionException;
import org.conservationmeasures.eam.project.OldVersionException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.calendar.CalendarView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.images.ImagesView;
import org.conservationmeasures.eam.views.interview.InterviewView;
import org.conservationmeasures.eam.views.map.MapView;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.task.TaskView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.util.DirectoryLock;

public class MainWindow extends JFrame implements CommandExecutedListener, ViewChangeListener, ClipboardOwner
{
	public MainWindow() throws IOException
	{
		this(new Project());
	}
	
	public MainWindow(Project projectToUse)
	{
		project = projectToUse;		
	}
	
	public void start(String[] args) throws Exception
	{
		project.addCommandExecutedListener(this);
		project.addViewChangeListener(this);

		actions = new Actions(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		mainStatusBar = new MainStatusBar();
		updateTitle();
		setSize(new Dimension(900, 700));
		setJMenuBar(mainMenuBar);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);

		addWindowListener(new WindowEventHandler());

		noProjectView = new NoProjectView(this);
		diagramView = new DiagramView(this);
		interviewView = new InterviewView(this);
		threatMatrixView = new ThreatMatrixView(this);
		budgetView = new BudgetView(this);
		taskView = new TaskView(this);
		mapView = new MapView(this);
		calendarView = new CalendarView(this);
		imagesView = new ImagesView(this);
		strategicPlanView = new StrategicPlanView(this);

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(interviewView, interviewView.cardName());
		viewHolder.add(threatMatrixView, threatMatrixView.cardName());
		viewHolder.add(budgetView, budgetView.cardName());
		viewHolder.add(taskView, taskView.cardName());
		viewHolder.add(mapView, mapView.cardName());
		viewHolder.add(calendarView, calendarView.cardName());
		viewHolder.add(imagesView, imagesView.cardName());
		viewHolder.add(strategicPlanView, strategicPlanView.cardName());
		getContentPane().add(viewHolder, BorderLayout.CENTER);
		
		setCurrentView(noProjectView);
		actions.updateActionStates();

		// TODO: allow --nosplash anywhere on command line
		if(args.length == 0 || !args[0].equals("--nosplash"))
		{
			Doer aboutDoer = diagramView.getDoer(ActionAbout.class);
			aboutDoer.doIt();
		}
		
		setVisible(true);
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
	
	private void setCurrentView(UmbrellaView view)
	{
		CardLayout layout = (CardLayout)viewHolder.getLayout();
		layout.show(viewHolder, view.cardName());
		currentView = view;
		updateToolBar();
	}

	public void updateToolBar()
	{
		UmbrellaView view = getCurrentView();
		if(view == null)
			return;
		JComponent toolBar = view.getToolBar();
		if(toolBar == null)
			throw new RuntimeException("View must have toolbar");
		toolBarBox.removeAll();
		toolBarBox.add(toolBar);
		toolBarBox.repaint();
	}

	public Project getProject()
	{
		return project;
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagramView.getDiagramComponent();
	}
	
	public Actions getActions()
	{
		return actions;
	}
	
	public void createOrOpenProject(File projectDirectory)
	{
		try
		{
			project.createOrOpen(projectDirectory);
			validate();
			updateTitle();
			updateStatusBar();
			getDiagramComponent().setModel(project.getDiagramModel());
			getDiagramComponent().requestFocus();
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
		}
		
		actions.updateActionStates();

	}
	
	public void closeProject() throws Exception
	{
		project.close();
		updateTitle();
		mainStatusBar.setStatus("");
	}
	
	public void updateStatusBar()
	{
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
		actions.updateActionStates();
		updateStatusBar();
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
	}
	
	public void commandFailed(Command command, CommandFailedException e)
	{
		mainStatusBar.setStatusError(e);
	}
	
	public void switchToView(String viewName)
	{
		if(viewName.equals(diagramView.cardName()))
			setCurrentView(diagramView);
		else if(viewName.equals(interviewView.cardName()))
			setCurrentView(interviewView);
		else if(viewName.equals(noProjectView.cardName()))
			setCurrentView(noProjectView);
		else if(viewName.equals(threatMatrixView.cardName()))
			setCurrentView(threatMatrixView);
		else if(viewName.equals(budgetView.cardName()))
			setCurrentView(budgetView);
		else if(viewName.equals(taskView.cardName()))
			setCurrentView(taskView);
		else if(viewName.equals(mapView.cardName()))
			setCurrentView(mapView);
		else if(viewName.equals(calendarView.cardName()))
			setCurrentView(calendarView);
		else if(viewName.equals(imagesView.cardName()))
			setCurrentView(imagesView);
		else if(viewName.equals(strategicPlanView.cardName()))
			setCurrentView(strategicPlanView);
		else
			EAM.logError("MainWindow.switchToView: Unknown view: " + viewName);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) 
	{
	}
	
	private void updateTitle()
	{
		setTitle(EAM.text("Title|CMP e-Adaptive Management") + " - " + project.getName());
	}
	
	class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			try
			{
				exitNormally();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				System.exit(1);
			}
		}
	}

	protected Actions actions;
	private Project project;
	private NoProjectView noProjectView;
	private DiagramView diagramView;
	private InterviewView interviewView;
	private ThreatMatrixView threatMatrixView;
	private BudgetView budgetView;
	private TaskView taskView;
	private MapView mapView;
	private CalendarView calendarView;
	private ImagesView imagesView;
	private StrategicPlanView strategicPlanView;
	
	private UmbrellaView currentView;
	private JPanel viewHolder;
	private JPanel toolBarBox;
	private MainMenuBar mainMenuBar;
	private MainStatusBar mainStatusBar;
}
