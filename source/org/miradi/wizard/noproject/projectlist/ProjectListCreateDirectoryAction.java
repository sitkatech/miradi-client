/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.wizard.noproject.projectlist;

import java.io.File;
import java.io.IOException;

import org.miradi.main.EAM;
import org.miradi.views.noproject.RenameProjectDoer;

public class ProjectListCreateDirectoryAction extends ProjectListAction
{
	public ProjectListCreateDirectoryAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, EAM.text("Create Folder"));
	}

	@Override
	protected void updateEnabledState()
	{
		try
		{
			boolean newState = !isProjectDirectory();
			setEnabled(newState);
			if (getSelectedFile() == null)
				setEnabled(false);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			setEnabled(false);
		}
	}
	
	@Override
	protected void doWork() throws Exception
	{	
		File newDirToCreate = new File(getSelectedFile(), "New Folder");
		if (!newDirToCreate.mkdirs())
			throw new IOException();
			
		RenameProjectDoer.doIt(EAM.getMainWindow(), newDirToCreate);
	}

	@Override
	protected String getErrorMessage()
	{
		return EAM.text("Error creating folder: ");
	}
}
