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

import org.conservationmeasures.eam.actions.Actions;

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(Actions actions) throws HeadlessException
	{
		add(createFileMenu(actions));
		add(createEditMenu(actions));
		add(createHelpMenu(actions));
	}

	private JMenu createFileMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.add(new JMenuItem(actions.newProject));
		menu.add(new JMenuItem(actions.openProject));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.exit));
		return menu;
	}

	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.add(new JMenuItem(actions.undo));
		menu.add(new JMenuItem(actions.redo));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.cut));
		menu.add(new JMenuItem(actions.copy));
		menu.add(new JMenuItem(actions.paste));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.delete));
		menu.add(new JMenuItem(actions.selectAll));
		return menu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(actions.about));
		return menu;
	}
}
