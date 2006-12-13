/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.io.File;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiOptionPane;
import org.martus.util.DirectoryLock;

public class RenameProject
{
	static public void doIt(MainWindow mainWindow, File projectToRename) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToRename))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToRename.getName());
			return;
		}
		
		DirectoryLock directoryLock = new DirectoryLock();

		if (!directoryLock.getDirectoryLock(projectToRename))
		{
			EAM.notifyDialog(EAM.text("Unable to rename this project because it is in use by another copy of this application:\n") +  projectToRename.getName());
			return;
		}

		try
		{
			String newName = UiOptionPane.showInputDialog("Enter New Project Name");
			File newFile = new File(projectToRename.getParentFile(),newName);
			
			if(ProjectServer.isExistingProject(newFile))
			{
				EAM.notifyDialog(EAM.text("Project by this name already exists, choose antother name."));
				return;
			}
			
			String[] body = {EAM.text("Are you sure you want to rename project: "), 
					projectToRename.getName(),
			};
			String[] buttons = {EAM.text("Rename"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Rename Project"), body, buttons))
				return;
		
			directoryLock.close();
			projectToRename.renameTo(newFile);
		}
		finally
		{
			directoryLock.close();
		}
	}
}
