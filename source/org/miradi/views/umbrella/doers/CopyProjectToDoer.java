/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

public class CopyProjectToDoer extends MainWindowDoer
{
	public boolean isAvailable()
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		while(true)
		{
			CreateProjectDialog dlg = new CreateProjectDialog(getMainWindow());
			if(!dlg.showCreateDialog(EAM.text("Copy Project To"), EAM.text("Button|Copy To")))
				return;

			File chosen = dlg.getSelectedFile();

			try
			{
				saveAsProject(chosen.getName());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				throw new CommandFailedException(
						"CopyProjectToDoer: Possible Write Protected:" + e);
			}

			return;
		}
	}

	private void saveAsProject(String newProjectName) throws Exception
	{
		File projectDirToCopy = getProject().getDatabase().getTopDirectory();
		File homeDir = EAM.getHomeDirectory();
		File tempZipFile = File.createTempFile("$$$" + newProjectName, ZIPFileFilter.EXTENSION);
		ProjectZipper.createProjectZipFile(tempZipFile, newProjectName, projectDirToCopy);
		ProjectUnzipper.unzipToProjectDirectory(tempZipFile, homeDir, newProjectName);
		tempZipFile.delete();
	}
}
