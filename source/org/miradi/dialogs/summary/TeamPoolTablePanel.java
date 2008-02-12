/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
