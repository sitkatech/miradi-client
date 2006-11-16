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
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.TncCapWorkbookImporter;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.martus.swing.UiFileChooser;



public class ImportTncCapWorkbookDoer extends ViewDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Project");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory);
		
		if (results.wasCancelChoosen())
			return;
		
		File fileToImport = results.getChosenFile();
		String projectName = withoutExtension(fileToImport.getName());
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
		
		boolean test = createProject(finalProjectDirectory, fileToImport, projectName);
		if (test)
			EAM.notifyDialog("Import complete");
		else
			EAM.errorDialog("Import failed");
	}

	private boolean createProject(File finalProjectDirectory, File importFile, String importFileName)
	{
		try
		{
			TncCapWorkbookImporter importer =new TncCapWorkbookImporter(importFile.getAbsolutePath());
			Project project = new Project();
			project.createOrOpen(finalProjectDirectory);
			project.setMetadata(ProjectMetadata.TAG_PROJECT_NAME, importFileName);
			project.setMetadata(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, importer.getProjectDownloadDate());
			project.setMetadata(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE, importer.getProjectVersionDate());
			project.setMetadata(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER, importer.getProjectVersion());
			project.close();
			NoProjectView noProjectView = (NoProjectView)getView();
			noProjectView.refreshText();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
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
