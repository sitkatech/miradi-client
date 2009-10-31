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
package org.miradi.views.umbrella.doers;

import java.io.File;

import org.martus.util.DirectoryUtils;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
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
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		CreateProjectDialog saveDialog = new CreateProjectDialog(getMainWindow(), EAM.text("Save As..."), getProject().getFilename());
		if(!saveDialog.showSaveAsDialog())
			return;

		File chosenFile = saveDialog.getSelectedFile();
		try
		{
			File newProjectDir = saveAs(chosenFile);
			getMainWindow().closeProject();
			ProjectListTreeTable.doProjectOpen(newProjectDir);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unexpected error during Save As: " + e);
		}
	}

	private File saveAs(File chosenFile) throws Exception
	{
		String newProjectName = getTrimmedFileName(chosenFile);
		File newProjectDir = new File(EAM.getHomeDirectory(), newProjectName);
		File tempZipFile = createTempProjectZip(newProjectName);
		try
		{			
			ProjectUnzipper.unzipToProjectDirectory(tempZipFile, EAM.getHomeDirectory(), newProjectName);
			
			return newProjectDir;
		}
		catch (Exception e)
		{
			DirectoryUtils.deleteEntireDirectoryTree(newProjectDir);
			throw e;
		}
		finally
		{
			tempZipFile.delete();
		}
	}

	private File createTempProjectZip(String newProjectName) throws Exception
	{
		File projectDirToCopy = getProject().getDatabase().getCurrentLocalProjectDirectory();
		File tempZipFile = File.createTempFile("$$$" + newProjectName, ZIPFileFilter.EXTENSION);
		ProjectZipper.createProjectZipFile(tempZipFile, newProjectName, projectDirToCopy);
		
		return tempZipFile;
	}
	
	private String getTrimmedFileName(File chosenFile)
	{
		return chosenFile.getName().trim();
	}
}
