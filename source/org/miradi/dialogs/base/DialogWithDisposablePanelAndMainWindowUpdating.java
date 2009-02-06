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

package org.miradi.dialogs.base;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.miradi.main.MainWindow;

public class DialogWithDisposablePanelAndMainWindowUpdating extends
		DialogWithDisposablePanel
{
	public DialogWithDisposablePanelAndMainWindowUpdating(MainWindow parent)
	{
		super(parent);
		mainWindow = parent;
		
		addWindowListener(new WindowEventHandler(mainWindow));
	}
	
	public DialogWithDisposablePanelAndMainWindowUpdating(MainWindow parent, DisposablePanel panelToUse)
	{
		this(parent);
		setMainPanel(panelToUse);
	}

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	static class WindowEventHandler implements WindowListener
	{
		public WindowEventHandler(MainWindow mainWindowToUse)
		{
			mainWindow = mainWindowToUse;
		}

		public void windowActivated(WindowEvent arg0)
		{
			mainWindow.updateActionStates();
		}
	
		public void windowClosed(WindowEvent arg0)
		{
		}
	
		public void windowClosing(WindowEvent arg0)
		{
		}
	
		public void windowDeactivated(WindowEvent arg0)
		{
		}
	
		public void windowDeiconified(WindowEvent arg0)
		{
		}
	
		public void windowIconified(WindowEvent arg0)
		{
		}
	
		public void windowOpened(WindowEvent arg0)
		{
			mainWindow.updateActionStates();
		}
		
		private MainWindow mainWindow;
	}
	

	private MainWindow mainWindow;
}
