/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;


import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.BudgetTableEditorComponent;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
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
		addField(createReadonlyTextField(Task.PSEUDO_TAG_BUDGET_TOTAL));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Text|Budget properties");
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		if (orefsToUse.length==0)
			tableEditorComponent.setTaskId(BaseId.INVALID);
		else
			tableEditorComponent.setTaskId(orefsToUse[0].getObjectId());
	}
	

	private void updateTable(CommandSetObjectData command)
	{
		if (wasAssignmentModified(command) || wasAssignmentAddedOrRemoved(command))
		{
			tableEditorComponent.dataWasChanged();
		}
	}

	private boolean wasAssignmentAddedOrRemoved(CommandSetObjectData command)
	{
		return command.getObjectType() == ObjectType.TASK && command.getFieldTag().equals(Task.TAG_ASSIGNMENT_IDS);
	}

	private boolean wasAssignmentModified(CommandSetObjectData command)
	{
		return command.getObjectType() == ObjectType.ASSIGNMENT;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable((CommandSetObjectData)event.getCommand());
	}
	
	BudgetTableEditorComponent tableEditorComponent;

}
