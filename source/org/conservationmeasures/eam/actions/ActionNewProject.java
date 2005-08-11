/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionNewProject extends MainWindowAction
{
	public ActionNewProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/new.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|New Project");
	}


	public void doAction(ActionEvent event) throws CommandFailedException
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(getLabel());
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText("Create new project");
		if(dlg.showDialog(mainWindow, EAM.text("Create")) != JFileChooser.APPROVE_OPTION)
			return;
		
		File chosen = dlg.getSelectedFile();
		if(chosen.exists())
		{
			String title = EAM.text("Title|Overwrite existing project?");
			String[] body = {EAM.text("This will replace the existing project with a new, empty project")};
			if(!EAM.confirmDialog(title, body))
				return;
			
			chosen.delete();
		}
				
		mainWindow.loadProject(chosen);
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new project");
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
}
