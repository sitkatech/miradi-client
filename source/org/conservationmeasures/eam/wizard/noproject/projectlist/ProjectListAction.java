/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.io.File;

import javax.swing.AbstractAction;

abstract class ProjectListAction extends AbstractAction
{
	public ProjectListAction(ProjectListTreeTable tableToUse, String string, File selectedFile)
	{
		super(string);
		table = tableToUse;
		thisFile = selectedFile;
	}

	File getFile()
	{
		return thisFile;
	}
	
	void refresh()
	{
		table.refresh();
	}
	
	private ProjectListTreeTable table;
	private File thisFile;
}