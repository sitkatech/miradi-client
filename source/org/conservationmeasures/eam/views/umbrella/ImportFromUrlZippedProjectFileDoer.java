/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.utils.Utility;
import org.martus.util.DirectoryUtils;

public class ImportFromUrlZippedProjectFileDoer 
{
	static public void doIt(MainWindow mainWindow) throws CommandFailedException 
	{
		String remotePath = "https://miradi.org/MarineExample.zip";
		
		File tempDir = null;
		try
		{
			URL remoteFile = new URL(remotePath);
			String newName = Utility.getFileNameWithoutExtension(remoteFile.getFile());
			newName = Project.makeProjectFilenameLegal(newName);
			Project.validateNewProject(newName);
			
			tempDir = File.createTempFile(TEMP_FILE_NAME, null);
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempDir));
			try
			{
				processCopy(outputStream, tempDir, remoteFile, newName);
			}
			finally 
			{
				outputStream.close();
				DirectoryUtils.deleteEntireDirectoryTree(tempDir);
			}
		}
		catch(Exception e)
		{
			String errorText = "Import Failed:" + e.getMessage();
			EAM.notifyDialog(EAM.text(errorText));
		}
	}


	private static void processCopy(OutputStream outputStream, File tempDir, URL remoteFile, String newName) throws IOException, Exception
	{
		InputStream inputStream = remoteFile.openConnection().getInputStream();
		try 
		{
			Utility.copy(inputStream, outputStream);
			ProjectUnzipper.unzipToProjectDirectory(tempDir, EAM.getHomeDirectory(), newName);
			EAM.notifyDialog(EAM.text("Import Completed"));
		}
		finally 
		{
			inputStream.close();
		}
	}
	
	static private String TEMP_FILE_NAME = "URLImport";
	
}
