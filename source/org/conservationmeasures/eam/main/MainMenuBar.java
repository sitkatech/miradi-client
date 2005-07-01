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

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(MainWindow mainWindow) throws HeadlessException
	{
		add(createFileMenu(mainWindow));
		add(createHelpMenu(mainWindow));
	}

	private JMenu createFileMenu(MainWindow mainWindow)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.add(new JMenuItem(new ActionExit(mainWindow)));
		return menu;
	}

	private JMenu createHelpMenu(MainWindow mainWindow)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(new ActionAbout(mainWindow)));
		return menu;
	}
}
