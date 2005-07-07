/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.martus.swing.UiNotifyDlg;

public class MainWindow extends JFrame
{
	public MainWindow() throws HeadlessException
	{
		project = new Project();
		
		setTitle(EAM.text("Title|CMP e-Adaptive Management"));
		setSize(new Dimension(700, 500));
		setJMenuBar(new MainMenuBar(this));
		getContentPane().add(new MainToolBar(), BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(new MainStatusBar(), BorderLayout.AFTER_LAST_LINE);

		getContentPane().add(new DiagramComponent(this));
	}
	
	public void recordCommand(Command command)
	{
		project.recordCommand(command);
	}
	
	public void okDialog(String title, String[] body)
	{
		new UiNotifyDlg(this, title, body, new String[] {EAM.text("Button|OK")});
	}
	
	public void exitNormally()
	{
		System.exit(0);
	}
	
	Project project;
}
