/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.utils.FileUtilities;

public class RenameProjectDoer
{
	public static void doIt(MainWindow mainWindow, File projectFileToRename) throws Exception 
	{
		try
		{
			String newProjectFileName = mainWindow.getDestinationProjectFileName(projectFileToRename);
			if (newProjectFileName == null)
				return;

			File newFile = new File(projectFileToRename.getParentFile(), newProjectFileName);
			FileUtilities.renameExistingWithRetries(projectFileToRename, newFile);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.notifyDialog(EAM.text("Rename Failed"));
		}
	}
	
}
