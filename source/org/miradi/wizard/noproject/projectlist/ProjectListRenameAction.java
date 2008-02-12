/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;

import org.miradi.main.EAM;
import org.miradi.views.noproject.RenameProjectDoer;

class ProjectListRenameAction extends ProjectListAction
{
	public ProjectListRenameAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Rename..."));
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			RenameProjectDoer.doIt(EAM.getMainWindow(), getFile());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error renaming project: " + e.getMessage()));
		}
		refresh();
	}
}