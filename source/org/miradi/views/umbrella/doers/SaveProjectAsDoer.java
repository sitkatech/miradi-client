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
package org.miradi.views.umbrella.doers;

import java.io.File;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.project.ProjectZipper;
import org.miradi.utils.ZIPFileFilter;
import org.miradi.views.MainWindowDoer;
import org.miradi.views.umbrella.CreateProjectDialog;
import org.miradi.wizard.noproject.projectlist.ProjectListTreeTable;

public class SaveProjectAsDoer extends MainWindowDoer
{
	public boolean isAvailable()
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		CreateProjectDialog dlg = new CreateProjectDialog(getMainWindow(), EAM.text("Save As..."));
		if(!dlg.showCreateDialog(EAM.text("Button|Save As...")))
			return;

		File chosen = dlg.getSelectedFile();
		try
		{
			saveAsAndOpen(chosen.getName());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unexpected error during Save As: " + e);
		}
	}

	private void saveAsAndOpen(String newProjectName) throws Exception
	{
		File projectDirToCopy = getProject().getDatabase().getTopDirectory();
		File homeDir = EAM.getHomeDirectory();
		File tempZipFile = File.createTempFile("$$$" + newProjectName, ZIPFileFilter.EXTENSION);
		ProjectZipper.createProjectZipFile(tempZipFile, newProjectName, projectDirToCopy);
		ProjectUnzipper.unzipToProjectDirectory(tempZipFile, homeDir, newProjectName);
		tempZipFile.delete();
		
		getMainWindow().closeProject();
		File newProjectDir = new File(homeDir, newProjectName);
		ProjectListTreeTable.doProjectOpen(newProjectDir);
	}
}
