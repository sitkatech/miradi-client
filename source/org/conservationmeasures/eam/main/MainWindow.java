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

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.interview.InterviewView;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MainWindow extends JFrame implements CommandExecutedListener, ViewChangeListener, ClipboardOwner
{
	public MainWindow()
	{
		project = new Project();
	}
	
	public void start()
	{
		project.addCommandExecutedListener(this);
		project.addViewChangeListener(this);

		actions = new Actions(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		MainStatusBar mainStatusBar = new MainStatusBar();

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

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(interviewView, interviewView.cardName());
		getContentPane().add(viewHolder, BorderLayout.CENTER);
		
		setCurrentView(noProjectView);
		actions.updateActionStates();

		setVisible(true);
	}

	private JComponent createCenteredView(JComponent viewToCenter)
	{
		// TODO: There *MUST* be a simpler way to center this view!
		viewToCenter.setAlignmentX(0.5f);
		viewToCenter.setAlignmentY(0.5f);
		
		Box centerHorizontally = Box.createVerticalBox();
		//centerHorizontally.setBorder(new LineBorder(Color.RED));
		centerHorizontally.add(viewToCenter);
		
		Box centerVertically = Box.createHorizontalBox();
		//centerVertically.setBorder(new LineBorder(Color.YELLOW));
		centerVertically.add(Box.createHorizontalGlue());
		centerVertically.add(centerHorizontally);
		centerVertically.add(Box.createHorizontalGlue());
		
		return centerVertically;
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
		toolBarBox.add(getCurrentView().getToolBar());
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
	
	public void loadProject(File projectFile)
	{
		try
		{
			project.load(this, projectFile);
			validate();
			updateTitle();
		}
		catch(IOException e)
		{
			EAM.logException(e);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
		
		actions.updateActionStates();

	}
	
	public void closeProject()
	{
		project.close();
	}

	public void exitNormally()
	{
		System.exit(0);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		actions.updateActionStates();
	}
	
	public void switchToView(String viewName)
	{
		if(viewName.equals(diagramView.cardName()))
			setCurrentView(diagramView);
		else if(viewName.equals(noProjectView.cardName()))
			setCurrentView(noProjectView);
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
			exitNormally();
		}
	}

	private Actions actions;
	private Project project;
	private NoProjectView noProjectView;
	private DiagramView diagramView;
	private InterviewView interviewView;
	private UmbrellaView currentView;
	private JPanel viewHolder;
	private JPanel toolBarBox;
	private MainMenuBar mainMenuBar;
}
