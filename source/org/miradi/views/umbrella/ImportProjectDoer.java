/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.views.ViewDoer;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.noproject.RenameProjectDoer;

public abstract class ImportProjectDoer extends ViewDoer
{
	public abstract void createProject(File importFile, File homeDirectory, String newProjectFilename)  throws Exception;
	
	public abstract FileFilter[] getFileFilters();

	public boolean isAvailable() 
	{
		Project project = getProject();
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;
		
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
			String projectName = RenameProjectDoer.getValidatedUserProjectName(getMainWindow(), fileToImport);
			if (projectName == null)
				return;
			
			fileToImport = EAMFileSaveChooser.getFileWithExtension(fileChooser, fileToImport);
			
			createProject(fileToImport, EAM.getHomeDirectory(), projectName);
			
			refreshNoProjectPanel();
			currentDirectory = fileToImport.getParent();
			showImportCompletedDialog();
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

	protected void showImportCompletedDialog() throws Exception
	{
		EAM.notifyDialog(EAM.text("Import Completed"));
	}

	private void addFileFilters(JFileChooser fileChooser)
	{
		FileFilter[] filters = getFileFilters();
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
