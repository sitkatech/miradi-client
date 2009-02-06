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
package org.miradi.views.noproject;

import java.io.File;

import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryUtils;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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
