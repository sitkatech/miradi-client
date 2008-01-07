/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.conservationmeasures.eam.actions.ActionDeleteTeamMember;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.base.ObjectTableModel;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class TeamEditorComponent extends ObjectTablePanel implements KeyListener
{
	public TeamEditorComponent(Project projectToUse, Actions actionsToUse)
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, new ObjectPoolTable(new TeamModel(projectToUse)));
		addDoubleClickAction(actionsToUse.get(ActionModifyResource.class));
		createButtonBar(actionsToUse);
		getTable().addKeyListener(this);
		actions = actionsToUse;
	}
	
	public void dispose()
	{
		super.dispose();
	}
	
	void createButtonBar(Actions actionsToUse)
	{
		addButton(new PanelButton(actionsToUse.get(ActionTeamCreateMember.class)));
		addButton(actionsToUse.getObjectsAction(ActionDeleteTeamMember.class));
		addButton(actionsToUse.getObjectsAction(ActionModifyResource.class));
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
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

	//TODO: Enter key should not cause a traversal event when used in this way....
	public void keyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyCode() ==KeyEvent.VK_ENTER)
		{
			if (getTable().getSelectedRow()<0) 
				return;
			try
			{
				actions.get(ActionModifyResource.class).doAction();
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}

	public void keyReleased(KeyEvent arg0)
	{
	}

	public void keyTyped(KeyEvent arg0)
	{
	}
	
	Actions actions;
}