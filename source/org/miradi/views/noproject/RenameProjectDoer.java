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
package org.miradi.views.noproject;

import java.io.File;
import java.io.IOException;

import org.martus.util.DirectoryLock;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.ModalRenameDialog;
import org.miradi.utils.Utility;
import org.miradi.wizard.noproject.WelcomeCreateStep;

public class RenameProjectDoer
{
	static public void doIt(MainWindow mainWindow, File projectToRename) throws Exception 
	{
		DirectoryLock directoryLock = new DirectoryLock();
		if (!directoryLock.getDirectoryLock(projectToRename))
		{
			EAM.notifyDialog(EAM.text("Unable to rename this project because it is in use by another copy of this application:\n") +  projectToRename.getName());
			return;
		}

		try
		{
			String newName = getLegalProjectNameFromUser(mainWindow, projectToRename.getName());
			if (newName == null)
				return;

			String[] body = {EAM.text("Are you sure you want to rename item: "), newName,
			};
			String[] buttons = {EAM.text("Rename"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Rename Item"), body, buttons))
				return;
		
			directoryLock.close();
			
			File newFile = new File(projectToRename.getParentFile(), newName);
			boolean wasRenamed = projectToRename.renameTo(newFile);
			if (!wasRenamed)
				throw new IOException("Item was not renamed.");
		}
		catch (Exception e)
		{
			EAM.notifyDialog("Rename Failed:" +e.getMessage());
		}
		finally
		{
			directoryLock.close();
		}
	}
	
	public static String getValidatedUserProjectName(MainWindow mainWindow, File fileToImport) throws Exception
	{
		String projectName = Utility.getFileNameWithoutExtension(fileToImport.getName());
		return getLegalProjectNameFromUser(mainWindow, projectName);
	}

	public static String getLegalProjectNameFromUser(MainWindow mainWindow, String projectName) throws Exception
	{
		while (true)
		{
			projectName = askUserForProjectName(mainWindow, projectName);
			if (projectName == null)
			{
				return null;
			}
			
			if (projectExists(projectName))
			{
				EAM.errorDialog(EAM.text("A project or file by this name already exists: ") + projectName);
				continue;
			}
			
			if (!Project.isValidProjectFilename(projectName))
			{
				EAM.errorDialog(EAM.text("Invalid project name:") + projectName);
				continue;
			}
			
			return projectName;
		}
	}
	
	private static boolean projectExists(String projectName)
	{
		File newFile = new File(EAM.getHomeDirectory(), projectName);
		if(ProjectServer.isExistingProject(newFile))
			return true;
		
		if(newFile.exists())
			return true;
		
		return false;
	}
	
	private static String askUserForProjectName(MainWindow mainWindow, String projectName) throws Exception
	{
		String legalProjectName = Project.makeProjectFilenameLegal(projectName);
		return ModalRenameDialog.showDialog(mainWindow, RenameProjectDoer.RENAME_TEXT, legalProjectName);
	}

	public static final String RENAME_TEXT = "<html>" + EAM.text("Enter New Name") + 
			"<br>&nbsp;&nbsp;&nbsp;<i>" + WelcomeCreateStep.getLegalProjectNameNote();
}
