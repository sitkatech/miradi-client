/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.swing.UiFileChooser;



public class ImportTncCapProjectFileDoer extends ViewDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Tnc Cap Project");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory);
		if (results.wasCancelChoosen())
			return;
		File zipToImport = results.getChosenFile();
		String projectName = withoutExtension(zipToImport.getName());
		File finalProjectDirectory = new File(EAM.getHomeDirectory(), projectName);
		if(ProjectServer.isExistingProject(finalProjectDirectory))
		{
			EAM.notifyDialog("Cannot import a project that already exists: " + projectName);
			return;
		}
		if(finalProjectDirectory.exists())
		{
			EAM.notifyDialog("Cannot import over an existing file or directory: " + 
					finalProjectDirectory.getAbsolutePath());
			return;
		}
		
		try
		{
				System.out.println("HERE");
				Project p = new Project();
				p.createOrOpen(finalProjectDirectory);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("Import failed");
		}
	
	}
	
	private String withoutExtension(String fileName)
	{
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		return fileName.substring(0, lastDotAt);
	}

}
