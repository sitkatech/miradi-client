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