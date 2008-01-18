/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.actions.ActionDeleteTeamMember;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

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
