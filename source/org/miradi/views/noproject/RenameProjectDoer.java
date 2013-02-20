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

import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.legacyprojects.LegacyProjectUtilities;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.ModalRenameDialog;
import org.miradi.wizard.noproject.WelcomeCreateStep;

public class RenameProjectDoer
{
	public static void doIt(MainWindow mainWindow, File projectFileToRename) throws Exception 
	{
		try
		{
			String newDirectoryName = getLegalMpfProjectFileNameFromUser(mainWindow, projectFileToRename);
			if (newDirectoryName == null)
				return;

			File newFile = new File(projectFileToRename.getParentFile(), newDirectoryName);
			FileUtilities.renameExistingWithRetries(projectFileToRename, newFile);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.notifyDialog(EAM.text("Rename Failed"));
		}
	}
	
	public static String getLegalMpfProjectFileNameFromUser(MainWindow mainWindow, File proposedProjectFile) throws Exception
	{
		while (true)
		{
			String projectName = askUserForProjectName(mainWindow, Project.withoutMpfProjectSuffix(proposedProjectFile.getName()));
			if (projectName == null)
			{
				return null;
			}

			String projectFileName =  AbstractMpfFileFilter.createNameWithExtension(projectName);
			proposedProjectFile = new File(EAM.getHomeDirectory(), projectFileName);
			if (projectExists(proposedProjectFile))
			{
				EAM.notifyDialog(EAM.substitute(EAM.text("A project or file by this name already exists: %s"), projectFileName));
				continue;
			}
			
			if (!Project.isValidProjectName(projectName))
			{
				EAM.errorDialog(EAM.substitute(EAM.text("Invalid project name: %s"), projectName));
				continue;
			}
			
			return projectFileName;
		}
	}
	
	private static boolean projectExists(File file) throws Exception
	{
		if(LegacyProjectUtilities.isExistingLocalProject(file))
			return true;
		
		if(file.exists())
			return true;
		
		return false;
	}
	
	private static String askUserForProjectName(MainWindow mainWindow, String projectName) throws Exception
	{
		String legalProjectName = Project.makeProjectNameLegal(projectName);
		return ModalRenameDialog.showDialog(mainWindow, RenameProjectDoer.RENAME_TEXT, legalProjectName);
	}

	public static final String RENAME_TEXT = "<html>" + EAM.text("Enter New Name") + 
			"<br>&nbsp;&nbsp;&nbsp;<i>" + WelcomeCreateStep.getLegalProjectNameNote();
}
