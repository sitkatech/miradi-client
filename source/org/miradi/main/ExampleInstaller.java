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
package org.miradi.main;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Vector;

import org.miradi.database.ProjectServer;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.CodeList;
import org.miradi.utils.MpzFileFilter;

public class ExampleInstaller
{
	public ExampleInstaller(AppPreferences appPreferencesToUse) throws Exception
	{
		appPreferences = appPreferencesToUse;
	}
	
	public void installSampleProjects() throws Exception
	{
		File[] allMpzFiles = getMpzFilesUnderAppDir();
		Vector<File> installableSampleProjects = getInstallableSampleProjects(allMpzFiles);
		if (userConfirmsSampleProjectsInstall(installableSampleProjects))
		{
			installSampleProjects(installableSampleProjects);
			EAM.notifyDialog(EAM.text("Sample Projects Installed"));
		}
	}

	private void installSampleProjects(Vector<File> installableSampleProjects) throws Exception
	{
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < installableSampleProjects.size(); ++index)
		{
			File projectFileToImport = installableSampleProjects.get(index);
			String validatedName = getValidatedProjectNameWithoutExtension(projectFileToImport);
			ProjectUnzipper.unzipToProjectDirectory(projectFileToImport, homeDir, validatedName);
		}
	}

	private boolean userConfirmsSampleProjectsInstall(Vector<File> installableSampleProjects)
	{
		if (installableSampleProjects.isEmpty())
			return false;
		
		String text = EAM.text("Do you want to install the sample projects");
		String[] buttonLabels = {EAM.getOkButtonText(), EAM.getCancelButtonText()};
			
		return EAM.confirmDialog(EAM.text("Install Sample Projects"), new String[]{text}, buttonLabels);
	}

	private Vector<File> getInstallableSampleProjects(File[] allMpzFiles)
	{
		CodeList installedSampleProjectCodes = getAppPreferences().getInstalledSampleProjectCodes();
		Vector<File> installableSampleProjects = new Vector(); 
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < allMpzFiles.length; ++index)
		{
			File mpzFile = allMpzFiles[index];
			String validatedName = getValidatedProjectNameWithoutExtension(mpzFile);
			File newProjectDir = new File(homeDir, validatedName);
			if (!ProjectServer.isExistingProject(newProjectDir) && !installedSampleProjectCodes.contains(validatedName))
			{
				installableSampleProjects.add(mpzFile);
				installedSampleProjectCodes.add(validatedName);
			}
		}
	
		getAppPreferences().setInstalledSampleProjectCodes(installedSampleProjectCodes);
		
		return installableSampleProjects;
	}

	private String getValidatedProjectNameWithoutExtension(File mpzFile)
	{
		String projectName = mpzFile.getName();
		String projectNameWithoutExtension = projectName.replaceFirst(MpzFileFilter.EXTENSION, "");
		return Project.makeProjectFilenameLegal(projectNameWithoutExtension); 
	}

	private File[] getMpzFilesUnderAppDir() throws URISyntaxException
	{
		File appDir = Miradi.getAppCodeDirectory();
		File[] files = appDir.listFiles(new MpzFileFilter());
		if (files == null)
			return new File[0];
		
		return files;
	}
	
	private AppPreferences getAppPreferences()
	{
		return appPreferences;
	}
	
	private AppPreferences appPreferences;
}
