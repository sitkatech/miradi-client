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
package org.miradi.dialogs.planning;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.project.Project;

//FIXME class under construction
abstract public class PlanningViewCustomButtonPanel extends DisposablePanel implements CommandExecutedListener
{
	public PlanningViewCustomButtonPanel(Project projectToUse)
	{
		project = projectToUse;
		
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		getProject().removeCommandExecutedListener(this);
	}

	public Project getProject()
	{
		return project;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
	}
	
	private Project project;
}
