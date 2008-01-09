/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;
import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;

class ProjectListExportAction extends ProjectListAction
{
	public ProjectListExportAction(ProjectListTreeTable tableToUse, File selectedFile)
	{
		super(tableToUse, EAM.text("Export..."), selectedFile);
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			ExportZippedProjectFileDoer.perform(EAM.getMainWindow(), getFile());
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error exporting project: " + e.getMessage()));
		}
	}
}