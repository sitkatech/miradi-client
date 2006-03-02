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
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.calendar.CalendarView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.interview.InterviewView;
import org.conservationmeasures.eam.views.map.MapView;
import org.conservationmeasures.eam.views.table.TableView;
import org.conservationmeasures.eam.views.task.TaskView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MainWindow extends JFrame implements CommandExecutedListener, ViewChangeListener, ClipboardOwner
{
	public MainWindow() throws IOException
	{
		project = new Project();
	}
	
	public void start() throws IOException
	{
		project.addCommandExecutedListener(this);
		project.addViewChangeListener(this);

		actions = new Actions(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		mainStatusBar = new MainStatusBar();
		updateTitle();
		setSize(new Dimension(700, 500));
		setJMenuBar(mainMenuBar);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);

		addWindowListener(new WindowEventHandler());

		noProjectView = new NoProjectView(this);
		diagramView = new DiagramView(this);
		interviewView = new InterviewView(this);
		tableView = new TableView(this);
		threatMatrixView = new ThreatMatrixView(this);
		budgetView = new BudgetView(this);
		taskView = new TaskView(this);
		mapView = new MapView(this);
		calendarView = new CalendarView(this);

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(interviewView, interviewView.cardName());
		viewHolder.add(tableView, tableView.cardName());
		viewHolder.add(threatMatrixView, threatMatrixView.cardName());
		viewHolder.add(budgetView, budgetView.cardName());
		viewHolder.add(taskView, taskView.cardName());
		viewHolder.add(mapView, mapView.cardName());
		viewHolder.add(calendarView, calendarView.cardName());
		getContentPane().add(viewHolder, BorderLayout.CENTER);
		
		setCurrentView(noProjectView);
		actions.updateActionStates();

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
		toolBarBox.removeAll();
		JToolBar toolBar = getCurrentView().getToolBar();
		if(toolBar == null)
			throw new RuntimeException("View must have toolbar");
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
		if(getProject().getLayerManager().areAllLayersVisible())
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
		else if(viewName.equals(tableView.cardName()))
			setCurrentView(tableView);
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

	private Actions actions;
	private Project project;
	private NoProjectView noProjectView;
	private DiagramView diagramView;
	private InterviewView interviewView;
	private TableView tableView;
	private ThreatMatrixView threatMatrixView;
	private BudgetView budgetView;
	private TaskView taskView;
	private MapView mapView;
	private CalendarView calendarView;
	
	private UmbrellaView currentView;
	private JPanel viewHolder;
	private JPanel toolBarBox;
	private MainMenuBar mainMenuBar;
	private MainStatusBar mainStatusBar;
}
