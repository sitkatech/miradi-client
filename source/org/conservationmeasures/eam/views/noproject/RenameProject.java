/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject;

import java.io.File;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiOptionPane;
import org.martus.util.DirectoryLock;

public class RenameProject
{
	static public void doIt(MainWindow mainWindow, File projectToRename) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToRename))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToRename.getName());
			return;
		}
		
		DirectoryLock directoryLock = new DirectoryLock();

		if (!directoryLock.getDirectoryLock(projectToRename))
		{
			EAM.notifyDialog(EAM.text("Unable to rename this project because it is in use by another copy of this application:\n") +  projectToRename.getName());
			return;
		}

		try
		{
			String newName = UiOptionPane.showInputDialog("Enter New Project Name");
			if (newName == null)
				return;

			Project.validateNewProject(newName);

			String[] body = {EAM.text("Are you sure you want to rename project: "), 
					projectToRename.getName(),
			};
			String[] buttons = {EAM.text("Rename"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Rename Project"), body, buttons))
				return;
		
			directoryLock.close();
			File newFile = new File(projectToRename.getParentFile(),newName);
			projectToRename.renameTo(newFile);
		}
		catch (Exception e)
		{
			EAM.notifyDialog("Rename Failed:" +e.getMessage());
		}
		finally
		{
			directoryLock.close();
		}
	}
}
