/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.files.MpfFileFilterWithoutDirectories;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.ProgressInterface;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class SampleInstaller
{
	public SampleInstaller(AppPreferences appPreferencesToUse) throws Exception
	{
		appPreferences = appPreferencesToUse;
		installedSampleProjectCodes = appPreferences.getInstalledSampleVersions();
	}
	
	public void installSampleProjects(ProgressInterface progressIndicator) throws Exception
	{
		File[] allMpfFiles = getMpfFilesUnderAppDir();
		Vector<File> installableSampleProjects = getInstallableSampleProjects(allMpfFiles);
		if (userConfirmsSampleProjectsInstall(installableSampleProjects))
		{
			installSampleProjects(installableSampleProjects, progressIndicator);
			getAppPreferences().setInstalledSampleVersion(installedSampleProjectCodes);
			EAM.notifyDialog(EAM.text("Sample Project Installed"));
		}
	}

	private void installSampleProjects(Vector<File> installableSampleProjects, ProgressInterface progressIndicator) throws Exception
	{
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < installableSampleProjects.size(); ++index)
		{
			File projectFileToImport = installableSampleProjects.get(index);
			String validatedName = getValidatedProjectNameWithoutExtension(projectFileToImport);
			
			File destination = new File(homeDir, AbstractMpfFileFilter.createNameWithExtension(validatedName));
			if(destination.exists())
				return;
			
			FileUtilities.copyFile(projectFileToImport, destination);
		}
	}

	private boolean userConfirmsSampleProjectsInstall(Vector<File> installableSampleProjects)
	{
		if (installableSampleProjects.isEmpty())
			return false;
		
		String text = EAM.text("Do you want to install the sample project?");
		String[] buttonLabels = {EAM.text("Button|Yes"), EAM.text("Button|No")};
			
		return EAM.confirmDialog(EAM.text("Install Sample Project"), new String[]{text}, buttonLabels);
	}

	private Vector<File> getInstallableSampleProjects(File[] allMpfFiles) throws Exception
	{
		if (installedSampleProjectCodes.contains(getVersionString()))
			return new Vector<File>();
		
		installedSampleProjectCodes.add(getVersionString());
		Vector<File> installableSampleProjects = new Vector<File>(); 
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < allMpfFiles.length; ++index)
		{
			File mpfFile = allMpfFiles[index];
			String validatedName = getValidatedProjectNameWithoutExtension(mpfFile);
			File newProjectFile = new File(homeDir, AbstractMpfFileFilter.createNameWithExtension(validatedName));
			if (FileSystemTreeNode.isProjectFile(newProjectFile) && !newProjectFile.exists())
				installableSampleProjects.add(mpfFile);
		}
		
		return installableSampleProjects;
	}

	private String getValidatedProjectNameWithoutExtension(File mpfProjectFile) throws Exception
	{
		String projectName = mpfProjectFile.getName();
		String projectNameWithoutExtension = Project.withoutMpfProjectSuffix(projectName);
		return Project.makeProjectNameLegal(projectNameWithoutExtension); 
	}

	private File[] getMpfFilesUnderAppDir() throws URISyntaxException
	{
		File appDir = Miradi.getAppCodeDirectory();
		File[] files = appDir.listFiles(new MpfFileFilterWithoutDirectories());
		if (files == null)
			return new File[0];
		
		return files;
	}
	
	private AppPreferences getAppPreferences()
	{
		return appPreferences;
	}
	
	private String getVersionString() throws Exception
	{
		return VersionConstants.getVersion();
	}
	
	private AppPreferences appPreferences;
	private CodeList installedSampleProjectCodes;
}
