/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectZipper;
import org.conservationmeasures.eam.utils.EAMFileSaveChooser;
import org.conservationmeasures.eam.utils.EAMZipFileChooser;
import org.conservationmeasures.eam.views.MainWindowDoer;



public class ExportZippedProjectFileDoer extends MainWindowDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		EAMFileSaveChooser eamFileChooser = new EAMZipFileChooser(getMainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		
		try 
		{
			zipFile(chosen); 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(EAM.text("Error Export To Zip: Possible Write Protected: ") + e);
		} 
	}

	private void zipFile(File out) throws Exception 
	{
		File projectDir = getProject().getDatabase().getTopDirectory();
		ProjectZipper.createProjectZipFile(out,projectDir);
	}
}
