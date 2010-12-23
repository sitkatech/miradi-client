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
import org.miradi.project.ProjectMpzWriter;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.MpzFileChooser;



public class ExportMpzFileDoer extends AbstractFileSaverDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return (getProject().getDatabase().isLocalProject());
	}
	
	@Override
	protected EAMFileSaveChooser createFileChooser()
	{
		return new MpzFileChooser(getMainWindow());
	}

	@Override
	protected void doWork(File destinationFile) throws Exception
	{
		export(getProject().getDatabase().getCurrentLocalProjectDirectory(), destinationFile); 
	}

	static public void perform(MainWindow mainWindow, File directoryToZip) throws CommandFailedException
	{
		EAMFileSaveChooser eamFileChooser = new MpzFileChooser(mainWindow);
		File chosen = eamFileChooser.displayChooser();
		if (chosen == null)
			return;
		
		export(directoryToZip, chosen);
	}

	private static void export(File directoryToZip, File chosen) throws CommandFailedException
	{
		if (isChosenFileInsideProjectHomeDir(chosen))
		{
			EAM.errorDialog(EAM.text("The MPZ file cannot be saved to a folder within the project being exported"));
			return;
		}
		
		try 
		{
			ProjectMpzWriter.createProjectZipFile(chosen, directoryToZip);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error Export To Miradi Zip: Possible Write Protected: "), e);
		}
	}

	private static boolean isChosenFileInsideProjectHomeDir(File chosen)
	{
		return EAM.isOneFileInsideTheOther(EAM.getHomeDirectory(), chosen);
	}
}
