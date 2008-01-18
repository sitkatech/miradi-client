/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.project.ProjectZipper;
import org.conservationmeasures.eam.utils.ZIPFileFilter;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.umbrella.CreateProjectDialog;

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
