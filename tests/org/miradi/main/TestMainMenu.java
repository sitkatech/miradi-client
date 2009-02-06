/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
