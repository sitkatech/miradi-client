/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.JLabel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.BudgetTableEditorComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTreeTablePanel;

public class BudgetPropertiesPanel extends ObjectDataInputPanel
{
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, BudgetTreeTablePanel treeTableComponent) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID, treeTableComponent);
	}
	
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow, BudgetTreeTablePanel treeTableComponent) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToShow);
		tableEditorComponent = new BudgetTableEditorComponent(projectToUse, actions, treeTableComponent);

		addField(createStringField(Task.TAG_LABEL));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_TASK_COST));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_SUBTASK_TOTAL));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_TASK_TOTAL));
		
		//FIXME remove empty component and make layoutmanager do the right thing.
		add(new JLabel());
		add(tableEditorComponent);
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Text|Budget properties");
	}
	
	public void setObjectId(BaseId newId)
	{
		super.setObjectId(newId);
		tableEditorComponent.setTaskId(newId);
	}

	private void updateTable(CommandSetObjectData command)
	{
		tableEditorComponent.dataWasChanged();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable((CommandSetObjectData)event.getCommand());
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable((CommandSetObjectData)event.getCommand());
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	BudgetTableEditorComponent tableEditorComponent;
}
