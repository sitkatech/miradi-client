/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.dialogfields.BudgetTableEditorComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;

public class BudgetPropertiesPanel extends ObjectDataInputPanel
{
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, WorkPlanPanel treeTableComponent) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID, treeTableComponent);
	}
	
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow, WorkPlanPanel treeTableComponent) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToShow);
		tableEditorComponent = new BudgetTableEditorComponent(projectToUse, actions, treeTableComponent);
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

	private void updateTable()
	{
		//FIXME budget code - should not be empty list
		//tableEditorComponent.setList(new IdList());
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		updateTable();
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		updateTable();
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	BudgetTableEditorComponent tableEditorComponent;
}
