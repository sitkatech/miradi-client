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
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryUtils;

public class DeleteProject
{
	static public void doIt(MainWindow mainWindow, File projectToDelete) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToDelete.getName());
			return;
		}
		
		DirectoryLock directoryLock = new DirectoryLock();

		if (!directoryLock.getDirectoryLock(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Unable to delete this project because it is in use by another copy of this application:\n") +  projectToDelete.getName());
			return;
		}

		try
		{
			String[] body = {EAM.text("Are you sure you want to delete this project? "), 
					projectToDelete.getName(),
			};
			String[] buttons = {EAM.text("Delete"), EAM.text("Keep"), };
			if(!EAM.confirmDialog(EAM.text("Delete Project"), body, buttons))
				return;
		}
		finally
		{
			directoryLock.close();
		}
		
		DirectoryUtils.deleteEntireDirectoryTree(projectToDelete);
	}
}
