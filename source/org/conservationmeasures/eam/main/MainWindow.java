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
import org.conservationmeasures.eam.diagram.DiagramToolBar;
import org.conservationmeasures.eam.exceptions.CommandFailedException;

public class MainWindow extends JFrame implements CommandExecutedListener, ClipboardOwner
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
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);

		addWindowListener(new WindowEventHandler());

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createNoProjectView(), NoProjectView.cardName());
		viewHolder.add(createDiagramView(), DiagramView.cardName());
		getContentPane().add(viewHolder, BorderLayout.CENTER);
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
	
	private JComponent createNoProjectView()
	{
		return createCenteredView(new NoProjectView(this));
	}

	private JComponent createDiagramView()
	{
		diagramView = new DiagramView(this);
		return diagramView;
	}
	
	private void setCurrentView(String name)
	{
		CardLayout layout = (CardLayout)viewHolder.getLayout();
		layout.show(viewHolder, name);
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
			mainToolBar = new DiagramToolBar(actions);
			toolBarBox.removeAll();
			toolBarBox.add(mainToolBar);

			setCurrentView("Diagram");
			
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

	public void exitNormally()
	{
		System.exit(0);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		actions.updateActionStates();
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
	private DiagramView diagramView;
	private JPanel viewHolder;
	private JPanel toolBarBox;
	private DiagramToolBar mainToolBar;
	private MainMenuBar mainMenuBar;
}
