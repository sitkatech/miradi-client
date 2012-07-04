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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.Utility;
import org.miradi.views.umbrella.CreateProjectDialog;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class CopyProject
{
	static public void doIt(MainWindow mainWindow, File projectToCopy) throws Exception 
	{
		if(!FileSystemTreeNode.isProjectFile(projectToCopy))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToCopy.getName());
			return;
		}
		
		try
		{
			CreateProjectDialog dlg = new CreateProjectDialog(mainWindow, EAM.text("Save As..."), projectToCopy.getName());
			if(!dlg.showSaveAsDialog())
				return;

			File chosen = dlg.getSelectedFile();
			String newName = chosen.getName();
			
			File newFile = new File(projectToCopy.getParentFile(),newName);
			Utility.copyFile(projectToCopy, newFile);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.notifyDialog("Save Failed with unexpected error: " +e.getMessage());
		}
	}


}