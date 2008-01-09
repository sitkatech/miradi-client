/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.noproject.DeleteProject;

class ProjectListDeleteAction extends ProjectListAction
{
	public ProjectListDeleteAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Delete"));
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			DeleteProject.doIt(EAM.getMainWindow(), getFile());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error deleting project: " + e.getMessage()));
		}
		refresh();
	}
}