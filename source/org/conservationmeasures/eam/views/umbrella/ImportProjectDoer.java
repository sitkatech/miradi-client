/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

public abstract class ImportProjectDoer extends ViewDoer
{
	public abstract void createProject(File importFile, File homeDirectory, String newProjectFilename)  throws Exception;
	
	public abstract String getFileExtension();
	
	public abstract FileFilter getFileFilter();

	public boolean isAvailable() 
	{
		Project project = getProject();
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		try
		{
			File startingDirectory = UiFileChooser.getHomeDirectoryFile();
			String windowTitle = EAM.text("Import Project");
			UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
					getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, getFileFilter());
			
			if (results.wasCancelChoosen())
				return;
			
			File fileToImport = results.getChosenFile();
			String projectName = withoutExtension(fileToImport.getName());
			projectName = Project.makeProjectFilenameLegal(projectName);
			File homeDirectory = EAM.getHomeDirectory();
			File finalProjectDirectory = new File(homeDirectory, projectName);
			
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
			
			createProject(fileToImport, homeDirectory, projectName);

			refreshNoProjectPanel();
			EAM.notifyDialog(EAM.text("Import Competed"));
		}
		catch(Exception e)
		{
			EAM.errorDialog("Import failed: " + e.getMessage());
			EAM.logException(e);
		}
	}

	private void refreshNoProjectPanel() throws Exception
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
