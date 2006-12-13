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
import org.martus.util.DirectoryUtils;

public class CopyProject
{
	static public void doIt(MainWindow mainWindow, File projectToCopy) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToCopy))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToCopy.getName());
			return;
		}
		
		DirectoryLock directoryLock = new DirectoryLock();

		if (!directoryLock.getDirectoryLock(projectToCopy))
		{
			EAM.notifyDialog(EAM.text("Unable to copy this project because it is in use by another copy of this application:\n") +  projectToCopy.getName());
			return;
		}

		try
		{
			String newName = UiOptionPane.showInputDialog("Enter New Project Name");
			if (newName == null)
				return;
			
			File newFile = new File(projectToCopy.getParentFile(),newName);
			
			if(ProjectServer.isExistingProject(newFile))
			{
				EAM.notifyDialog(EAM.text("Project by this name already exists, choose antother name."));
				return;
			}
			
			if (!mainWindow.getProject().isValidProjectFilename(newName))
			{
				EAM.notifyDialog(EAM.text("Invalid project name, choose antother name."));
				return;
			}
			
			String[] body = {EAM.text("Are you sure you want to copy project: "), 
					projectToCopy.getName(),
			};
			String[] buttons = {EAM.text("Copy"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Copy Project"), body, buttons))
				return;
		
			directoryLock.close();
			DirectoryUtils.copyDirectoryTree(projectToCopy, newFile);
		}
		finally
		{
			directoryLock.close();
		}
	}


}