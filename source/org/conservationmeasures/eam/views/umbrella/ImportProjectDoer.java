/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Utility;
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
			File startingDirectory = new File(currentDirectory);
			String windowTitle = EAM.text("Import Project");
			UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
					getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, getFileFilter());
			
			if (results.wasCancelChoosen())
				return;
			
			File fileToImport = results.getChosenFile();
			String projectName = Utility.getFileNameWithoutExtension(fileToImport.getName());
			projectName = Project.makeProjectFilenameLegal(projectName);
			Project.validateNewProject(projectName);

			createProject(fileToImport, EAM.getHomeDirectory(), projectName);
			refreshNoProjectPanel();
			currentDirectory = fileToImport.getParent();
			EAM.notifyDialog(EAM.text("Import Competed"));
		}
		catch(Exception e)
		{
			EAM.errorDialog("Import failed: " + e.getMessage());
		}
	}

	private void refreshNoProjectPanel() throws Exception
	{
		NoProjectView noProjectView = (NoProjectView)getView();
		noProjectView.refreshText();
	}
	
	private static String currentDirectory = UiFileChooser.getHomeDirectoryFile().getPath();

}
