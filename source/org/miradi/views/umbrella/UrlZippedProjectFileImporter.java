/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.martus.util.DirectoryUtils;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.Utility;

public class UrlZippedProjectFileImporter 
{
	static public void importMarineExample(MainWindow mainWindow) throws CommandFailedException 
	{
		String remotePath = "https://miradi.org/MarineExample.zip";
		
		try
		{
			importRemoteProjectZip(remotePath);
		}
		catch(Exception e)
		{
			String errorText = "Import Failed:" + e.getMessage();
			EAM.notifyDialog(EAM.text(errorText));
		}
	}

	private static void importRemoteProjectZip(String remotePath) throws MalformedURLException, Exception, IOException, FileNotFoundException
	{
		URL remoteFileURL = new URL(remotePath);
		String newName = Utility.getFileNameWithoutExtension(remoteFileURL.getFile());
		newName = Project.makeProjectFilenameLegal(newName);
		Project.validateNewProject(newName);
		
		File tempFile = File.createTempFile(TEMP_FILE_NAME, null);
		try 
		{
			downloadFile(tempFile, remoteFileURL);
			ProjectUnzipper.unzipToProjectDirectory(tempFile, EAM.getHomeDirectory(), newName);
			EAM.notifyDialog(EAM.text("Import Completed"));
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempFile);
		}
	}

	private static void downloadFile(File destFile, URL remoteFileURL) throws FileNotFoundException, IOException, Exception
	{
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
		try
		{
			downloadFile(outputStream, remoteFileURL);
		}
		finally 
		{
			outputStream.close();
		}
	}

	private static void downloadFile(OutputStream outputStream, URL remoteFileURL) throws IOException, Exception
	{
		InputStream inputStream = remoteFileURL.openConnection().getInputStream();
		try 
		{
			Utility.copy(inputStream, outputStream);
		}
		finally 
		{
			inputStream.close();
		}
	}
	
	static private String TEMP_FILE_NAME = "URLImport";
	
}
