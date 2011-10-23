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
package org.miradi.main;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeWriter;
import org.miradi.project.MpzToDotMiradiConverter;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.MpfFileFilter;
import org.miradi.utils.MpzFileFilter;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class SampleInstaller
{
	public SampleInstaller(AppPreferences appPreferencesToUse) throws Exception
	{
		appPreferences = appPreferencesToUse;
		installedSampleProjectCodes = appPreferences.getInstalledSampleVersions();
	}
	
	public void installSampleProjects() throws Exception
	{
		File[] allMpzFiles = getMpzFilesUnderAppDir();
		Vector<File> installableSampleProjects = getInstallableSampleProjects(allMpzFiles);
		if (userConfirmsSampleProjectsInstall(installableSampleProjects))
		{
			installSampleProjects(installableSampleProjects);
			getAppPreferences().setInstalledSampleVersion(installedSampleProjectCodes);
			EAM.notifyDialog(EAM.text("Sample Project Installed"));
		}
	}

	private void installSampleProjects(Vector<File> installableSampleProjects) throws Exception
	{
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < installableSampleProjects.size(); ++index)
		{
			File projectFileToImport = installableSampleProjects.get(index);
			String validatedName = getValidatedProjectNameWithoutExtension(projectFileToImport);
			
			File destination = new File(homeDir, validatedName + MpfFileFilter.EXTENSION);
			if(destination.exists())
				throw new RuntimeException(".Miradi file already exists: " + destination.getAbsolutePath());
		
			convertToMpf(projectFileToImport, destination);
		}
	}

	public void convertToMpf(File projectFileToImport, File destination) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(destination);
		try
		{
			String converted = MpzToDotMiradiConverter.convert(new ZipFile(projectFileToImport));
			writer.write(converted);
		}
		finally
		{
			writer.close();
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

	private Vector<File> getInstallableSampleProjects(File[] allMpzFiles) throws Exception
	{
		if (installedSampleProjectCodes.contains(getVersionString()))
			return new Vector<File>();
		
		installedSampleProjectCodes.add(getVersionString());
		Vector<File> installableSampleProjects = new Vector<File>(); 
		File homeDir = EAM.getHomeDirectory();
		for (int index = 0; index < allMpzFiles.length; ++index)
		{
			File mpzFile = allMpzFiles[index];
			String validatedName = getValidatedProjectNameWithoutExtension(mpzFile);
			File newProjectFile = new File(homeDir, validatedName + MpfFileFilter.EXTENSION);
			if (FileSystemTreeNode.isProjectFile(newProjectFile))
				installableSampleProjects.add(mpzFile);
		}
		
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
	
	private String getVersionString() throws Exception
	{
		return VersionConstants.getVersion();
	}
	
	private AppPreferences appPreferences;
	private CodeList installedSampleProjectCodes;
}
