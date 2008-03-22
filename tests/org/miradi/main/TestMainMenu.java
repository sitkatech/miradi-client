/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.main;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.miradi.actions.Actions;
import org.miradi.main.MainWindow;
import org.miradi.main.menu.MainMenuBar;


public class TestMainMenu extends EAMTestCase
{
	public TestMainMenu(String name)
	{
		super(name);
	}
	
	public void testFileMenu() throws Exception
	{
		Actions actions = new Actions(new MainWindow());
		JMenuBar menuBar = new MainMenuBar(actions);
		JMenu fileMenu = menuBar.getMenu(0);
		JMenuItem exitItem = fileMenu.getItem(fileMenu.getItemCount() - 1);
		Action exitAction = exitItem.getAction();
		assertEquals("Exit", exitAction.getValue(Action.NAME));
	}
}
