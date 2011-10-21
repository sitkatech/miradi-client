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

import org.miradi.main.EAM;
import org.miradi.views.noproject.RenameProjectDoer;

class ProjectListRenameAction extends ProjectListAction
{
	public ProjectListRenameAction(ProjectListTreeTable tableToUse)
	{
		super(tableToUse, getButtonLabel());
	}

	@Override
	protected void updateEnabledState()
	{
		try
		{
			boolean enable = isProjectSelected() || isDirectorySelected(); 
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
		if(isOldProjectSelected() || isDirectorySelected())
			RenameProjectDoer.doIt(EAM.getMainWindow(), getSelectedFile());
		else
			EAM.notifyDialog("Renaming projects is not supported yet");
	}
	
	@Override
	protected String getErrorMessage()
	{
		return EAM.text("Error renaming project: ");
	}
	
	private static String getButtonLabel()
	{
		return EAM.text("Rename...");
	}
}