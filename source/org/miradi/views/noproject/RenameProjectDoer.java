/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.martus.swing.UiOptionPane;
import org.martus.util.DirectoryLock;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;

public class RenameProjectDoer
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
			String newName = askUserForProjectName(projectToRename.getName());
			if (newName == null)
				return;

			String[] body = {EAM.text("Are you sure you want to rename project: "), newName,
			};
			String[] buttons = {EAM.text("Rename"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Rename Project"), body, buttons))
				return;
		
			directoryLock.close();
			File newFile = new File(projectToRename.getParentFile(),newName);
			projectToRename.renameTo(newFile);
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

	public static String askUserForProjectName(String initialValue) throws Exception
	{
		String newName = UiOptionPane.showInputDialog("Enter New Project Name", initialValue);
		if (newName == null)
			return null;

		Project.validateNewProject(newName);
		return newName;
	}
}
