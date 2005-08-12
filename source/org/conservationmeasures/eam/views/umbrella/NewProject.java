/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class NewProject extends MainWindowDoer
{
	public NewProject(MainWindow mainWindow)
	{
		super(mainWindow);
	}
	
	public void doIt() throws CommandFailedException
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(EAM.text("Title|Create New Project"));
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText("Create new project");
		if(dlg.showDialog(getMainWindow(), EAM.text("Create")) != JFileChooser.APPROVE_OPTION)
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
				
		getMainWindow().loadProject(chosen);
	}

	public boolean isAvailable()
	{
		return true;
	}

}
