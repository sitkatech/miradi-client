/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.noproject.NoProjectView;



public class ImportZipFileDoer extends MainWindowDoer
{
	public boolean isAvailable() 
	{
		Project project = getProject();
		if(project.isOpen())
			return false;
		return !project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		NoProjectView noProjectView = (NoProjectView)getMainWindow().getCurrentView();
		noProjectView.doImportZip();
	}


}
