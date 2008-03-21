/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.summary;

import org.miradi.actions.ActionDeleteTeamMember;
import org.miradi.actions.ActionTeamCreateMember;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class TeamPoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public TeamPoolTablePanel(Project project, Actions actions)
	{
		super(project, new TeamPoolTable(new TeamPoolTableModel(project)), 
				actions,
				buttons);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectResource.getObjectType(), ProjectResource.TAG_ROLE_CODES))
			getTable().getObjectTableModel().rowsWereAddedOrRemoved();
	}
	
	static Class[] buttons = new Class[] {
		ActionTeamCreateMember.class,
		ActionDeleteTeamMember.class,
	};
}
