/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.martus.swing.UiFileChooser;

public abstract class ImportDoer extends ViewDoer
{
	public abstract boolean createProject(File finalProjectDirectory, File importFile);
	
	public abstract String getFileExtension();
	
	public abstract FileFilter getFileFilter();

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
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, getFileFilter());
		
		if (results.wasCancelChoosen())
			return;
		
		File fileToImport = results.getChosenFile();
		String projectName = withoutExtension(fileToImport.getName());
		File finalProjectDirectory = new File(EAM.getHomeDirectory(), projectName);
		
		if(ProjectServer.isExistingProject(finalProjectDirectory))
		{
			EAM.notifyDialog(EAM.text("Cannot import a project that already exists: ") + projectName);
			return;
		}
		
		if(finalProjectDirectory.exists())
		{
			EAM.notifyDialog(EAM.text("Cannot import over an existing file or directory: ") + 
					finalProjectDirectory.getAbsolutePath());
			return;
		}
		
		if (!createProject(finalProjectDirectory, fileToImport))
		{
			EAM.notifyDialog(EAM.text("Import failed"));
			return;
		}

		refreshNoProjectPanel();
		EAM.notifyDialog(EAM.text("Import Competed"));
	}

	private void refreshNoProjectPanel()
	{
		NoProjectView noProjectView = (NoProjectView)getView();
		noProjectView.refreshText();
	}

	private String withoutExtension(String fileName)
	{
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		return fileName.substring(0, lastDotAt);
	}

}
