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

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.martus.util.DirectoryUtils;

public class ImportFromUrlZippedProjectFileDoer 
{
	static public void doIt(MainWindow mainWindow) throws CommandFailedException 
	{
		String remotePath = "https://miradi.org/MarineExample.zip";
		
		OutputStream outputStream = null;
		InputStream inputStream = null;
		File tempDir = null;
		try
		{
			tempDir = File.createTempFile(TEMP_FILE_NAME, null);
			URL remoteFile = new URL(remotePath);
			String newName = getFileNameWithoutExtension(remoteFile.getFile());
			String errorText = Project.validateNewProject(newName);
			if (errorText.length()>0)
			{
				errorText = "Import Failed:" + errorText;
				EAM.notifyDialog(EAM.text(errorText));
				return;
			}
			
			outputStream = new BufferedOutputStream(new FileOutputStream(tempDir));
			inputStream = remoteFile.openConnection().getInputStream();
			copy(inputStream, outputStream);
			ProjectUnzipper.unzipToProjectDirectory(tempDir, EAM.getHomeDirectory(), newName);
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


	
	//TODO: this is a buffered stream copy method, it should moved to utils
	static private void copy(InputStream inputStream, OutputStream outputStream) throws Exception
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
	
	//TODO: this  method, should moved to utils
	static private String getFileNameWithoutExtension(String name)
	{
		String fileName = new File(name).getName();
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		
		return fileName.substring(0, lastDotAt);
	}
	

	//FIXME: can be coded better
	static private void cleanUp(OutputStream outputStream, InputStream inputStream, File tempDir)
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

	

	static private String TEMP_FILE_NAME = "URLImport";
	
}
