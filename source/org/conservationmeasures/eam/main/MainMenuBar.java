/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionOpenProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionUndo;

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(MainWindow mainWindow) throws HeadlessException
	{
		add(createFileMenu(mainWindow));
		add(createEditMenu(mainWindow));
		add(createHelpMenu(mainWindow));
	}

	private JMenu createFileMenu(MainWindow mainWindow)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.add(new JMenuItem(new ActionNewProject(mainWindow)));
		menu.add(new JMenuItem(new ActionOpenProject(mainWindow)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionExit(mainWindow)));
		return menu;
	}

	private JMenu createEditMenu(MainWindow mainWindow)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.add(new JMenuItem(new ActionUndo(mainWindow)));
		menu.add(new JMenuItem(new ActionRedo(mainWindow)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionCut(mainWindow)));
		menu.add(new JMenuItem(new ActionCopy(mainWindow)));
		menu.add(new JMenuItem(new ActionPaste(mainWindow)));
		menu.addSeparator();
		menu.add(new JMenuItem(new ActionDelete(mainWindow)));
		menu.add(new JMenuItem(new ActionSelectAll(mainWindow)));
		return menu;
	}

	private JMenu createHelpMenu(MainWindow mainWindow)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(new ActionAbout(mainWindow)));
		return menu;
	}
}
