/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.BudgetTableEditorComponent;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class BudgetPropertiesInputPanel extends ObjectDataInputPanel
{
	public BudgetPropertiesInputPanel(Project projectToUse, Actions actions, BaseId idToShow, BudgetTableEditorComponent tableEditorComponentToUse) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToShow);
		tableEditorComponent = tableEditorComponentToUse;

		addField(createStringField(Task.TAG_LABEL));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_TASK_COST));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_SUBTASK_TOTAL));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_TASK_TOTAL));
		
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
