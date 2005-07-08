/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFrame;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.martus.swing.UiNotifyDlg;

public class MainWindow extends JFrame
{
	public MainWindow() throws HeadlessException
	{
		project = new Project();

		updateTitle();
		setSize(new Dimension(700, 500));
		setJMenuBar(new MainMenuBar(this));
		getContentPane().add(new MainToolBar(this), BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(new MainStatusBar(), BorderLayout.AFTER_LAST_LINE);

		getContentPane().add(new DiagramComponent(this, project.getDiagramModel()));
	}
	
	public void loadProject(File projectFile)
	{
		project.load(projectFile);
		updateTitle();
	}
	
	public void recordCommand(Command command)
	{
		project.recordCommand(command);
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
	
	private void updateTitle()
	{
		setTitle(EAM.text("Title|CMP e-Adaptive Management") + " - " + project.getName());
	}
	
	Project project;
}
