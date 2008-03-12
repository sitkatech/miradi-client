/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		fileChooser.setApproveButtonToolTipText(EAM.text("Choose Data Directory"));
		if (fileChooser.showDialog(mainWindow, EAM.text("Choose Directory")) != JFileChooser.APPROVE_OPTION)
			return null;

		return fileChooser.getSelectedFile();
	}
	
	private MainWindow mainWindow;
	private static final String DIALOG_TITLE = EAM.text("Directory Chooser");
}
