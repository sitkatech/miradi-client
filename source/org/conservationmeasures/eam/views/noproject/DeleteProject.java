/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryUtils;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class DeleteProject
{
	static public void doIt(MainWindow mainWindow, File projectToDelete) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToDelete.getName());
			return;
		}
		
		if (isProjectOpened(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Unable to delete this project because it is in use by another copy of this application:\n") +  projectToDelete.getName());
			return;
		}
		
		String[] body = {"Are you sure you want to delete this project? ", 
				projectToDelete.getName(),
		};
		String[] buttons = {"Delete", "Keep", };
		if(!EAM.confirmDialog("Delete Project", body, buttons))
			return;

		DirectoryUtils.deleteEntireDirectoryTree(projectToDelete);
		
		EAM.notifyDialog(EAM.text("Delete Competed"));
	}
	
	//FIXME: this code should not be duplicated here but called from the LockDirectory
	static private boolean isProjectOpened(File directory) throws IOException
	{
		DirectoryLock lock = new DirectoryLock();
		try
		{
			lock.lock(directory);
			return false;
		}
		catch(AlreadyLockedException e)
		{
			return true;
		}
		finally
		{
			lock.close();
		}
	}

}
