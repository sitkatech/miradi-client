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

public class ImporFromUrlZippedProjectFileDoer 
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
			String newName = getFileNameWithoutExtension(new File(remoteFile.getFile()).getName());
			
			if(ProjectServer.isExistingProject(newFile))
			{
				EAM.notifyDialog(EAM.text("Project by this name already exists, can not import."));
				return;
			}
			
			if (!mainWindow.getProject().isValidProjectFilename(newName))
			{
				String text = "Invalid project name, can not import:" + newName;
				EAM.notifyDialog(EAM.text(text));
				return;
			}
			
			if(newFile.exists())
			{
				String text = EAM.text("Cannot import over an existing file or directory: ") + newFile.getAbsolutePath();
				EAM.notifyDialog(EAM.text(text));
				return;
			}
			
			outputStream = new BufferedOutputStream(new FileOutputStream(tempDir));
			inputStream = remoteFile.openConnection().getInputStream();
			copy(inputStream, outputStream);
			ProjectUnzipper.unzipToProjectDirectory(tempDir, homeDirectory, newName);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cleanUp(outputStream, inputStream, tempDir);
		}
	}
	
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

	private String getFileNameWithoutExtension(String fileName)
	{
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		
		return fileName.substring(0, lastDotAt);
	}
	

	private static String TEMP_FILE_NAME = "URLImport";
	
}
