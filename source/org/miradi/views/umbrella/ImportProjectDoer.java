/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.umbrella;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.martus.swing.UiFileChooser;
import org.miradi.database.ProjectServer;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.ModalRenameDialog;
import org.miradi.utils.Utility;
import org.miradi.views.ViewDoer;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.noproject.RenameProjectDoer;

public abstract class ImportProjectDoer extends ViewDoer
{
	public abstract void createProject(File importFile, File homeDirectory, String newProjectFilename)  throws Exception;
	
	public abstract FileFilter[] getFileFilter();

	public boolean isAvailable() 
	{
		Project project = getProject();
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		try
		{
			JFileChooser fileChooser = new JFileChooser(currentDirectory);
			fileChooser.setDialogTitle(EAM.text("Import Project"));
			addFileFilters(fileChooser);
			
			fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			fileChooser.setApproveButtonToolTipText(EAM.text(getApproveButtonToolTipText()));
			if (fileChooser.showDialog(getMainWindow(), getDialogApprovelButtonText()) != JFileChooser.APPROVE_OPTION)
				return;
			
			File fileToImport = fileChooser.getSelectedFile();
			String projectName = getValidatedUserProjectName(getMainWindow(), fileToImport);
			if (projectName == null)
				return;
			
			createProject(fileToImport, EAM.getHomeDirectory(), projectName);
			
			refreshNoProjectPanel();
			currentDirectory = fileToImport.getParent();
			EAM.notifyDialog(EAM.text("Import Completed"));
		}
		catch (UnsupportedNewVersionSchemaException e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(IMPORT_FAILED_MESSAGE);
		}
		catch (ValidationException e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(e.getMessage());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(e.getMessage());
		}
	}
	
	public static String getValidatedUserProjectName(MainWindow mainWindow, File fileToImport) throws Exception
	{
		String projectName = Utility.getFileNameWithoutExtension(fileToImport.getName());
		return getValidatedUserProjectName(mainWindow, projectName);
	}

	public static String getValidatedUserProjectName(MainWindow mainWindow, String projectName) throws Exception
	{
		while (true)
		{
			projectName = askUserForProjectName(mainWindow, projectName);
			if (projectName == null)
			{
				return null;
			}
			
			if (projectExists(projectName))
			{
				EAM.errorDialog(EAM.text("A project or file by this name already exists: ") + projectName);
				continue;
			}
			
			if (!isLegalValidProjectName(mainWindow.getProject(), projectName))
			{
				EAM.errorDialog(EAM.text("Invalid project name:") + projectName);
				continue;
			}
			
			return projectName;
		}
	}
	
	private static boolean projectExists(String projectName)
	{
		File newFile = new File(EAM.getHomeDirectory(), projectName);
		if(ProjectServer.isExistingProject(newFile))
			return true;
		
		if(newFile.exists())
			return true;
		
		return false;
	}
	
	private static boolean isLegalValidProjectName(Project project, String projectName)
	{
		if (project.isValidProjectFilename(projectName))
			return true;
			
		if (EAM.isLegalFileName(projectName))
			return true;
		
		return false;
	}
	
	private static String askUserForProjectName(MainWindow mainWindow, String projectName) throws Exception
	{
		String legalProjectName = Project.makeProjectFilenameLegal(projectName);
		String newName = ModalRenameDialog.showDialog(mainWindow, RenameProjectDoer.RENAME_TEXT, legalProjectName);
		if (newName == null)
			return null;

		return newName;
	}

	private void addFileFilters(JFileChooser fileChooser)
	{
		FileFilter[] filters = getFileFilter();
		for (int i = 0; i < filters.length; ++i)
		{
			fileChooser.addChoosableFileFilter(filters[i]);
		}
	}

	private void showImportFailedErrorDialog(String message)
	{
		String safeMessage = EAM.substitute(EAM.text("<html>Import failed: <br><p> %s </p></html>"), message);
		EAM.errorDialog(safeMessage);
	}

	private String getApproveButtonToolTipText()
	{
		return "Import";
	}
	
	private String getDialogApprovelButtonText()
	{
		return "Import";
	}
	
	private void refreshNoProjectPanel() throws Exception
	{
		NoProjectView noProjectView = (NoProjectView)getView();
		noProjectView.refreshText();
	}
	
	private static String currentDirectory = UiFileChooser.getHomeDirectoryFile().getPath();
	private static final String IMPORT_FAILED_MESSAGE = EAM.text("This file cannot be imported because it is a newer format than this version of Miradi supports. <br>" +
			  "Please make sure you are running the latest version of Miradi. If you are already <br>" +
			  "running the latest Miradi, either wait for a newer version that supports this format, <br>" +
			  "or re-export the project to an older (supported) format.");
			
}
