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

package org.miradi.dialogs.notify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class NeverAgainCheckBoxHandler implements ActionListener
{
	public NeverAgainCheckBoxHandler(MainWindow mainWindowToUse, String notifyDialogCodeToUse)
	{
		mainWindow = mainWindowToUse;
		notifyDialogCode = notifyDialogCodeToUse;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox checkBox = (JCheckBox) event.getSource();
		boolean isChecked = checkBox.isSelected();
		AppPreferences preferences = mainWindow.getAppPreferences();
		if(isChecked)
		{
			preferences.neverShowNotifyDialogAgain(notifyDialogCode);
		}
		else
		{
			preferences.showNotifyDialogAgain(notifyDialogCode);
		}
		
		try
		{
			mainWindow.savePreferences();
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private MainWindow mainWindow;
	private String notifyDialogCode;
}