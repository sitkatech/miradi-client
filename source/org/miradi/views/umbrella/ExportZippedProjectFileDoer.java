/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		if (chosen==null) return;
		
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


}
