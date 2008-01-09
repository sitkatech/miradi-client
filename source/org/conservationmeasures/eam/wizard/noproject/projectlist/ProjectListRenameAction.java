/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;
import java.io.File;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.noproject.RenameProject;

class ProjectListRenameAction extends ProjectListAction
{
	public ProjectListRenameAction(ProjectListTreeTable tableToUse, File selectedFile)
	{
		super(tableToUse, EAM.text("Rename..."), selectedFile);
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			RenameProject.doIt(EAM.getMainWindow(), getFile());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error renaming project: " + e.getMessage()));
		}
		refresh();
	}
}