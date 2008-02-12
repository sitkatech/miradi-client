/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.views.umbrella.ExportZippedProjectFileDoer;

class ProjectListExportAction extends ProjectListAction
{
	public ProjectListExportAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Export..."));
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