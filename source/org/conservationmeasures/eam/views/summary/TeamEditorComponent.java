/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.ObjectTableModel;
import org.conservationmeasures.eam.dialogs.ObjectTablePanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

public class TeamEditorComponent extends ObjectTablePanel
{
	public TeamEditorComponent(Project projectToUse, Actions actions)
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, new ObjectPoolTable(new TeamModel(projectToUse)));
		addDoubleClickAction(actions.get(ActionModifyResource.class));
		createButtonBar(actions);
	}
	
	public void dispose()
	{
		super.dispose();
	}
	
	void createButtonBar(Actions actions)
	{
		addButton(new UiButton(actions.get(ActionTeamCreateMember.class)));
		addButton(actions.getObjectsAction(ActionTeamRemoveMember.class));
		addButton(actions.getObjectsAction(ActionModifyResource.class));
		addButton(new UiButton(actions.get(ActionViewPossibleTeamMembers.class)));
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		notifyTableRoleCodeChange(event);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		notifyTableRoleCodeChange(event);
	}
	
	private void notifyTableRoleCodeChange(CommandExecutedEvent event)
	{
		if (!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		if (((CommandSetObjectData) event.getCommand()).getObjectType() != ObjectType.PROJECT_RESOURCE)
			return;
		
		if (((CommandSetObjectData) event.getCommand()).getFieldTag().equals(ProjectResource.TAG_ROLE_CODES))
			((ObjectTableModel) getTable().getModel()).rowsWereAddedOrRemoved();
	}
}