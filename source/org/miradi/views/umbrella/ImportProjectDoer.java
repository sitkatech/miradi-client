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
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
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
			String windowTitle = EAM.text("Import Project");
			JFileChooser dlg = new JFileChooser(currentDirectory);

			dlg.setDialogTitle(windowTitle);
			FileFilter[] filters = getFileFilter();
			for (int i=0; i<filters.length; ++i)
				dlg.addChoosableFileFilter(filters[i]);
			dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
			dlg.setApproveButtonToolTipText(EAM.text(getApproveButtonToolTipText()));
			if (dlg.showDialog(getMainWindow(), getDialogApprovelButtonText()) != JFileChooser.APPROVE_OPTION)
				return;
			
			File fileToImport = dlg.getSelectedFile();
			String projectName = Utility.getFileNameWithoutExtension(fileToImport.getName());
			String newName = RenameProjectDoer.askUserForProjectName(projectName);
			if (newName == null)
				return;
			
			newName = Project.makeProjectFilenameLegal(newName);
			createProject(fileToImport, EAM.getHomeDirectory(), newName);
			refreshNoProjectPanel();
			currentDirectory = fileToImport.getParent();
			EAM.notifyDialog(EAM.text("Import Completed"));
		}
		catch(Exception e)
		{
			EAM.errorDialog("Import failed: " + e.getMessage());
		}
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

}
