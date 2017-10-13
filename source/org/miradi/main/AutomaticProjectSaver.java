/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import java.io.IOException;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.FileLocker;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.Utility;

public class AutomaticProjectSaver implements CommandExecutedListener
{
	public AutomaticProjectSaver(Project projectToTrack)
	{
		project = projectToTrack;
		getProject().addCommandExecutedListener(this);
		locker = new FileLocker();
	}
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void startSaving(File projectFileToUse) throws Exception
	{
		locker.lock(getLockFile(projectFileToUse));
		setProjectFile(projectFileToUse);
		ensureNewlyCreatedProjectFileExists();
		ensureSingleSessionProjectFile();
	}
	
	private void ensureSingleSessionProjectFile() throws Exception
	{
		File sessionFile = getSessionFile(getProjectFile());
		FileUtilities.deleteIfExistsWithRetries(sessionFile);
		Utility.copyFile(getProjectFile(), sessionFile);
	}

	private void ensureNewlyCreatedProjectFileExists() throws Exception
	{
		if (getProjectFile().exists())
			return;

		safeSave();
	}

	public void stopSaving() throws Exception
	{
		setProjectFile(null);
		locker.close();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if(getProjectFile() == null)
			return;
		
		if(getProject().isInTransaction())
			return;
		
		try
		{
			safeSave();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public void safeSave() throws Exception
	{
		internalSafeSave(getProjectFile());
	}

	public void internalSafeSave(File currentFile) throws IOException, Exception
	{
		if(currentFile == null)
			return;
		
		File oldFile = getOldFile(currentFile);
		File newFile = getNewFile(currentFile);

		FileUtilities.deleteIfExistsWithRetries(newFile);
		save(newFile);

		FileUtilities.deleteIfExistsWithRetries(oldFile);
		FileUtilities.renameIfExistsWithRetries(currentFile, oldFile);

		FileUtilities.renameExistingWithRetries(newFile, currentFile);
		
		// NOTE: recovery steps:
		// 1. if valid new file exists, use it, else
		// 2. if valid current file exists, use it, else
		// 3. if valid old file exists, use it
	}
	
	public static File getSessionFile(final File currentFile)
	{
		return FileUtilities.getFileWithSuffix(currentFile, SESSION_EXTENSION);
	}

	public static File getOldFile(File currentFile)
	{
		return FileUtilities.getFileWithSuffix(currentFile, OLD_EXTENSION);
	}

	public static File getNewFile(File currentFile)
	{
		return FileUtilities.getFileWithSuffix(currentFile, NEW_EXTENSION);
	}

	public static File getLockFile(File currentFile)
	{
		return FileUtilities.getFileWithSuffix(currentFile, ".lock");
	}

	private void save(File file) throws Exception
	{
		long startedAt = System.currentTimeMillis();
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		ProjectSaver.saveProject(getProject(), stringWriter);
		
		UnicodeWriter fileWriter = new UnicodeWriter(file);
		fileWriter.write(stringWriter.toString());
		fileWriter.close();
		long endedAt = System.currentTimeMillis();
		EAM.logDebug("Saved project: " + (endedAt - startedAt) + "ms");
	}

	private Project getProject()
	{
		return project;
	}
	
	private File getProjectFile()
	{
		return projectFile;
	}
	
	private void setProjectFile(final File projectFileToUse)
	{
		projectFile = projectFileToUse;
	}
	
	private File projectFile;
	private Project project;
	private FileLocker locker;
	
	public final static String SESSION_EXTENSION = ".session";
	public final static String OLD_EXTENSION = ".old";
	public final static String NEW_EXTENSION = ".new";
}
