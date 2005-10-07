/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class NewProject extends MainWindowDoer
{
	public void doIt() throws CommandFailedException
	{
		while(true)
		{
			ProjectChooser dlg = new ProjectChooser(getMainWindow());
			if(!dlg.showCreateDialog())
				return;
	
			File chosen = dlg.getSelectedFile();
			if(chosen.exists())
			{
				String body = EAM.text("Cannot overwrite an existing file or directory");
				EAM.errorDialog(body);
				continue;
			}

			String name = chosen.getName();
			if(!Project.isValidProjectName(name))
			{
				String body = EAM.text("Project names cannot contain punctuation other than dots, dashes, and spaces");
				EAM.errorDialog(body);
				continue;
			}

			getMainWindow().loadProject(chosen);
				return;
		}
				
	}

	public boolean isAvailable()
	{
		return true;
	}

}
