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

import org.miradi.main.AutomaticProjectSaver;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FileUtilities;

public class DeleteProject
{
	static public void doIt(MainWindow mainWindow, File projectFileToDelete) throws Exception 
	{
		String[] body = {getDeleteMessage(projectFileToDelete), projectFileToDelete.getName(),	};
		String[] buttons = {EAM.text("Delete"), EAM.text("Keep"), };
		if(!EAM.confirmDialog(EAM.text("Delete"), body, buttons))
			return;
		
		projectFileToDelete.delete();
		deleteReleatedProjectFiles(projectFileToDelete);
	}

	private static void deleteReleatedProjectFiles(File projectFileToDelete) throws Exception
	{
		FileUtilities.deleteIfExists(AutomaticProjectSaver.getSessionFile(projectFileToDelete));
		FileUtilities.deleteIfExists(AutomaticProjectSaver.getOldFile(projectFileToDelete));
		FileUtilities.deleteIfExists(AutomaticProjectSaver.getLockFile(projectFileToDelete));
		FileUtilities.deleteIfExists(AutomaticProjectSaver.getNewFile(projectFileToDelete));
	}

	private static String getDeleteMessage(File projectFileToDelete)
	{
		return EAM.text("Are you sure you want to delete this Miradi project?");
	}
}
