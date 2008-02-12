/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard.noproject.projectlist;

import java.awt.event.ActionEvent;

import org.miradi.main.EAM;

class ProjectListOpenAction extends ProjectListAction
{
	public ProjectListOpenAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Open"));
		setEnabled(ProjectListTreeTable.isProjectDirectory(getFile()));
	}

	public void actionPerformed(ActionEvent event)
	{
		ProjectListTreeTable.doProjectOpen(getFile());
	}
}