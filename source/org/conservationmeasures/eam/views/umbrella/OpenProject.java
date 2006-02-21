/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class OpenProject extends MainWindowDoer
{
	public void doIt() throws CommandFailedException
	{
		ProjectChooser dlg = new ProjectChooser(getMainWindow());
		if(!dlg.showOpenDialog())
			return;
		
		File chosen = dlg.getSelectedFile();
		getMainWindow().createOrOpenProject(chosen);
	}
	
	public boolean isAvailable()
	{
		return true;
	}

}
