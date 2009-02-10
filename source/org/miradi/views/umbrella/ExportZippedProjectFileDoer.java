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

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.project.ProjectZipper;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMZipFileChooser;
import org.miradi.views.MainWindowDoer;



public class ExportZippedProjectFileDoer extends MainWindowDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;

		perform(getMainWindow(), getProject().getDatabase().getTopDirectory()); 
	}

	static public void perform(MainWindow mainWindow, File directoryToZip) throws CommandFailedException
	{
		EAMFileSaveChooser eamFileChooser = new EAMZipFileChooser(mainWindow);
		File chosen = eamFileChooser.displayChooser();
		if (chosen == null)
			return;
		
		if (isChosenFileInsideProjectHomeDir(chosen))
		{
			EAM.errorDialog(EAM.text("The MPZ file cannot be saved to a folder within the project being exported"));
			return;
		}
		
		try 
		{
			ProjectZipper.createProjectZipFile(chosen, directoryToZip);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error Export To Miradi Zip: Possible Write Protected: ") + e);
		}
	}

	private static boolean isChosenFileInsideProjectHomeDir(File chosen)
	{
		return EAM.isOneFileInsideTheOther(EAM.getHomeDirectory(), chosen);
	}
}
