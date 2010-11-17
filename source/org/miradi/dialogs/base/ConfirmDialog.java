/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.miradi.main.EAM;

public class ConfirmDialog
{
	public static boolean confirm(JDialog owner, String title, String yesButtonText, String noButtonText)
	{
		String[] buttonLabels = new String[] {yesButtonText, noButtonText};
		int button = JOptionPane.showOptionDialog(owner, title, EAM.text("Confirmation"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonLabels, null);
		if (button == 0)
			return true;
		
		return false;
	}
	
	public static boolean confirmWithinThread(JDialog owner, String title, String yesButtonText, String noButtonText)
	{
		ThreadedConfirmDialogLauncher launcher = new ThreadedConfirmDialogLauncher(owner, title, yesButtonText, noButtonText);
		try
		{
			SwingUtilities.invokeAndWait(launcher);
		}
		catch(InterruptedException e)
		{
			EAM.unexpectedErrorDialog(e);
		}
		catch(InvocationTargetException e)
		{
			EAM.unexpectedErrorDialog(e);
		}
		return launcher.confirmed();
	}
	
	public static class ThreadedConfirmDialogLauncher implements Runnable
	{
		public ThreadedConfirmDialogLauncher(JDialog ownerToUse, String titleToUse, String yesButtonText, String noButtonText)
		{
			owner = ownerToUse;
			title = titleToUse;
			yesText = yesButtonText;
			noText = noButtonText;
		}
		
		public void run()
		{
			result = confirm(owner, title, yesText, noText);
		}

		public boolean confirmed()
		{
			return result;
		}
		
		private JDialog owner;
		private String title;
		private String yesText;
		private String noText;
		private boolean result;
	}

}
