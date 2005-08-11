/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.martus.swing.UiNotifyDlg;
import org.martus.swing.UiScrollPane;

public class MainWindow extends JFrame implements CommandExecutedListener
{
	public MainWindow()
	{
		project = new Project();
	}
	
	public void start()
	{
		project.addCommandExecutedListener(this);

		actions = new Actions(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		MainStatusBar mainStatusBar = new MainStatusBar();

		updateTitle();
		setSize(new Dimension(700, 500));
		setJMenuBar(mainMenuBar);
		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);

		addWindowListener(new WindowEventHandler());

		activateNoProjectView();
		setVisible(true);
	}

	private void activateNoProjectView()
	{
		NoProjectView noProjectView = new NoProjectView(this);
		JPanel containerForCentering = new JPanel();
		containerForCentering.setBorder(new LineBorder(Color.RED));
		containerForCentering.add(noProjectView, BorderLayout.CENTER);
		noProjectView.setAlignmentX(0.5f);
		noProjectView.setAlignmentY(0.5f);
		setCurrentView(containerForCentering);
	}

	public Project getProject()
	{
		return project;
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagramComponent;
	}
	
	public Actions getActions()
	{
		return actions;
	}
	
	public boolean isProjectOpen()
	{
		return project.isOpen();
	}
	
	public void loadProject(File projectFile)
	{
		clearCurrentView();
		
		try
		{
			mainToolBar = new DiagramToolBar(actions);
			toolBarBox.removeAll();
			toolBarBox.add(mainToolBar);

			diagramComponent = new DiagramComponent(this, project.getDiagramModel());
			setCurrentView(new UiScrollPane(diagramComponent));
			
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

	private void clearCurrentView()
	{
		if(currentView != null)
		{
			getContentPane().remove(currentView);
		}
	}
	
	public void recordCommand(Command command)
	{
		project.recordCommand(command);
	}
	
	public void errorDialog(String errorMessage)
	{
		okDialog("Error", new String[] {errorMessage});
	}
	
	public void okDialog(String title, String[] body)
	{
		new UiNotifyDlg(this, title, body, new String[] {EAM.text("Button|OK")});
	}
	
	public boolean confirmDialog(String title, String[] body)
	{
		String[] buttons = { EAM.text("Button|Overwrite"), EAM.text("Button|Cancel") };
		UiNotifyDlg dlg = new UiNotifyDlg(this, title, body, buttons);
		return (dlg.getResult().equals(buttons[0]));
	}
	
	public void exitNormally()
	{
		System.exit(0);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		actions.updateActionStates();
	}
	
	private void setCurrentView(JComponent view)
	{
		currentView = view;
		getContentPane().add(currentView);
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

	Actions actions;
	Project project;
	JComponent currentView;
	DiagramComponent diagramComponent;
	private JPanel toolBarBox;
	private DiagramToolBar mainToolBar;
	private MainMenuBar mainMenuBar;
}
