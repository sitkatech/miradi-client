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

import java.io.File;

import javax.swing.JFileChooser;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class MiradiDirectoryChooser
{
	public MiradiDirectoryChooser(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
		
	public File displayChooser() throws CommandFailedException
	{	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle(DIALOG_TITLE);
		fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		fileChooser.setApproveButtonToolTipText(EAM.text("Choose Data Folder"));
		if (fileChooser.showDialog(mainWindow, EAM.text("Choose Folder")) != JFileChooser.APPROVE_OPTION)
			return null;

		return fileChooser.getSelectedFile();
	}
	
	private MainWindow mainWindow;
	private static final String DIALOG_TITLE = EAM.text("Folder Chooser");
}
