/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.util.DirectoryUtils;

public class DeleteProject
{
	static public void doIt(MainWindow mainWindow, File projectToDelete) throws CommandFailedException, FileNotFoundException 
	{
		Vector dialogText = new Vector();
		
		dialogText.add("\nAre you sure you want to delete this project? " + projectToDelete.getName());

		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Accounting Code", (String[])dialogText.toArray(new String[0]), buttons))
			return;
		
		if(!ProjectServer.isExistingProject(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToDelete.getName());
			return;
		}

		if (isProjectOpened(projectToDelete))
		{
			EAM.notifyDialog(EAM.text("Cannot delete an opened project: ") +  projectToDelete.getName());
			return;
		}
		
		EAM.notifyDialog(EAM.text("Delete Competed"));
		
		DirectoryUtils.deleteEntireDirectoryTree(projectToDelete);
		
	}
	
	//FIXME: this code should not be duplicated here but called from the LockDirectory
	static private boolean isProjectOpened(File directory) throws FileNotFoundException
	{
	// Always return true????
	//	File lockFile = new File(directory, "lock");
	//	FileOutputStream tempLockStream = new FileOutputStream(lockFile);
	//	return tempLockStream.getChannel().isOpen();
		return false;
	}

}
