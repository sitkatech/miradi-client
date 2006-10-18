/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.project.ProjectZipper;
import org.conservationmeasures.eam.utils.ZIPFileFilter;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class SaveAsProjectDoer extends MainWindowDoer
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
			if(!dlg.showCreateDialog("Save As"))
				return;

			File chosen = dlg.getSelectedFile();

			try
			{
				saveAsProject(chosen.getName());
			}
			catch(Exception e)
			{
				throw new CommandFailedException(
						"SaveAsProjectDoer: Possible Write Protected:" + e);
			}

			return;
		}
	}

	private void saveAsProject(String newProjectName) throws Exception
	{
		File projectDirTocpy = getProject().getDatabase().getTopDirectory();
		File homeDir = EAM.getHomeDirectory();
		File tempZipFile = File.createTempFile(newProjectName, ZIPFileFilter.ZIP_EXTENSION);
		ProjectZipper.createProjectZipFile(tempZipFile,  "/" + newProjectName, projectDirTocpy);
		ProjectUnzipper.unzipToProjectDirectory(tempZipFile, homeDir);
		tempZipFile.delete();
	}
}
