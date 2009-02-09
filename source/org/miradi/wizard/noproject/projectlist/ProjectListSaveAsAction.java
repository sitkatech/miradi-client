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

import java.awt.event.ActionEvent;

import org.miradi.main.EAM;
import org.miradi.views.noproject.CopyProject;

class ProjectListSaveAsAction extends ProjectListAction
{
	public ProjectListSaveAsAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, getButtonLabel());
		
		updateEnabledState();
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			doWork();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(getErrorMessage() + e.getMessage());
		}
		refresh();
	}
	
	private void doWork() throws Exception
	{
		CopyProject.doIt(EAM.getMainWindow(), getFile());
	}

	private String getErrorMessage()
	{
		return EAM.text("Error copying project: ");
	}

	private static String getButtonLabel()
	{
		return EAM.text("Save As...");
	}
}