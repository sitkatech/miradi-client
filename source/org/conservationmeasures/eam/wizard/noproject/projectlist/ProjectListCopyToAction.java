/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;
import java.io.File;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.noproject.CopyProject;

class ProjectListCopyToAction extends ProjectListAction
{
	public ProjectListCopyToAction(ProjectListTreeTable tableToUse, File selectedFile)
	{
		super(tableToUse, EAM.text("Copy To..."), selectedFile);
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			CopyProject.doIt(EAM.getMainWindow(), getFile());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error copying project: " + e.getMessage()));
		}
		refresh();
	}
}