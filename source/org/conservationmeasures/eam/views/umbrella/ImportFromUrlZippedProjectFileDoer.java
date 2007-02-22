/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.martus.util.DirectoryUtils;

public class ImportFromUrlZippedProjectFileDoer 
{
	public void doIt(MainWindow mainWindow) throws CommandFailedException 
	{
		String remotePath = "https://miradi.org/MarineExample.zip";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		File tempDir = null;
		try
		{
			tempDir = File.createTempFile(TEMP_FILE_NAME, null);
			URL remoteFile = new URL(remotePath);
			

			File homeDirectory = EAM.getHomeDirectory();
			File newFile = new File(homeDirectory,getFileNameWithoutExtension(remoteFile.getFile()));
			String newName = getFileNameWithoutExtension(remoteFile.getFile());
			
			String errorText = validateNewProject(mainWindow, newFile, newName);
			if (errorText.length()>0)
			{
				errorText = "Import Failed:" + errorText;
				EAM.notifyDialog(EAM.text(errorText));
				return;
			}
			
			outputStream = new BufferedOutputStream(new FileOutputStream(tempDir));
			inputStream = remoteFile.openConnection().getInputStream();
			copy(inputStream, outputStream);
			ProjectUnzipper.unzipToProjectDirectory(tempDir, homeDirectory, newName);
			EAM.notifyDialog(EAM.text("Import Completed"));
			
		}
		catch(Exception e)
		{
			EAM.logException(e);
			String errorText = "Import Failed:" + e.getMessage();
			EAM.notifyDialog(EAM.text(errorText));
		}
		finally
		{
			cleanUp(outputStream, inputStream, tempDir);
		}
	}

	//TODO: This code is used by several importes , rename and copy routines and should be made common
	private String validateNewProject(MainWindow mainWindow, File newFile, String newName)
	{
		if(ProjectServer.isExistingProject(newFile))
			return "Project by this name already exists:" + newName;
		
		if (!mainWindow.getProject().isValidProjectFilename(newName))
			return "Invalid project name:" + newName;
		
		if(newFile.exists())
			return "A file or directory exist by the same name:" + newName;
		
		return "";
	}
	
	//TODO: this is a buffered stream copy method, it should moved to utils
	public void copy(InputStream inputStream, OutputStream outputStream) throws Exception
	{
		byte[] buffer = new byte[1024];
		int numRead;
		long numWritten = 0;
		while ((numRead = inputStream.read(buffer)) != -1) 
		{
			outputStream.write(buffer, 0, numRead);
			numWritten += numRead;
		}
	}

	//FIXME: can be coded better
	private void cleanUp(OutputStream outputStream, InputStream inputStream, File tempDir)
	{
		try
		{
			if(inputStream != null)
				inputStream.close();
			if(outputStream != null)
				outputStream.close();
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
		catch(Exception ioe)
		{
		}
	}

	private String getFileNameWithoutExtension(String name)
	{
		String fileName = new File(name).getName();
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		
		return fileName.substring(0, lastDotAt);
	}
	

	private static String TEMP_FILE_NAME = "URLImport";
	
}
