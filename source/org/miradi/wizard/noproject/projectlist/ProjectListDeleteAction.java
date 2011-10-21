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

import org.miradi.icons.DeleteIcon;
import org.miradi.main.EAM;
import org.miradi.views.noproject.DeleteProject;

class ProjectListDeleteAction extends ProjectListAction
{
	public ProjectListDeleteAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, getButtonLabel(), new DeleteIcon());
	}

	@Override
	protected void updateEnabledState()
	{
		try
		{
			boolean enable = isProjectSelected() || isEmptyDirectorySelected(); 
			if(EAM.getHomeDirectory().equals(getSelectedFile()))
				enable = false;
			setEnabled(enable);
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
		if(isProjectSelected())
			EAM.notifyDialog("Deleting project is not supported yet");
		else if(isOldProjectSelected() || isDirectorySelected())
			DeleteProject.doIt(EAM.getMainWindow(), getSelectedFile());
	}
	
	@Override
	protected String getErrorMessage()
	{
		return EAM.text("Error deleting project: ");
	}

	private static String getButtonLabel()
	{
		return EAM.text("Delete");
	}
}