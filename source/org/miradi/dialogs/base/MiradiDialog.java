/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.miradi.main.AppPreferences;


public class MiradiDialog extends JDialog
{
	public MiradiDialog(JFrame parent)
	{
		super(parent);
		
		initializeDialog();
	}

	public MiradiDialog(JDialog owner)
	{
		super(owner);
		
		initializeDialog();
	}
	
	private void initializeDialog()
	{
		getContentPane().setBackground(AppPreferences.getDarkPanelBackgroundColor());
		addWindowListener(new WindowEventHandler(this));
	}

	class WindowEventHandler implements WindowListener
	{
		public WindowEventHandler(Window windowToMonitor)
		{
			window = windowToMonitor;
		}
		
		public void windowActivated(WindowEvent arg0)
		{
		}

		public void windowClosed(WindowEvent arg0)
		{
		}

		public void windowClosing(WindowEvent arg0)
		{
			// NOTE: We are not sure that windowClosing should call dispose,
			// because some code retrieves data from the dialog later
			window.dispose();
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
			// MRD-6127 - workaround for JDK-8190522 (& related)
			window.removeWindowListener(this);
			window.toFront();
		}
		
		private Window window;
	}
}
