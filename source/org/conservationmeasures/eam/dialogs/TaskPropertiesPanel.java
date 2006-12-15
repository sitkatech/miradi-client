/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.conservationmeasures.eam.views.workplan.AssignmentEditorComponent;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID);
	}
	
	public TaskPropertiesPanel(Project projectToUse, Actions actions, ObjectPicker objectPicker) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID, objectPicker);
	}
	
	public TaskPropertiesPanel(Project projectToUse, Actions actions, BaseId idToEdit, ObjectPicker objectPicker) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		editorComponent = new AssignmentEditorComponent(actions, project, objectPicker);
		
		add(editorComponent);
		addCommonFields();
	}

	public TaskPropertiesPanel(Project projectToUse, Actions actions, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		
		addCommonFields();
	}
	
	private void addCommonFields()
	{
		addField(createReadonlyTextField(Task.PSEUDO_TAG_FACTOR_LABEL));
		addField(createStringField(Task.TAG_LABEL));
		
		updateFieldsFromProject();
	}
	
	public void setObjectId(BaseId id)
	{
		super.setObjectId(id);
		if (editorComponent == null)
			return;
	
		editorComponent.setTaskId(id);
	}
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	private void updateTable()
	{
		if (editorComponent == null)
			return;
		
		editorComponent.dataWasChanged();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable();
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable();
	}
	
	public void commandFailed(Command command, CommandFailedException e)
	{
		super.commandFailed(command, e);
	}
	
		
	Project project;
	AssignmentEditorComponent editorComponent;
	
}

class FakePicker implements ObjectPicker
{
	public void addListSelectionListener(ListSelectionListener listener)
	{
	}
	
	public void clearSelection()
	{
	}
	
	public EAMObject[] getSelectedObjects()
	{
			return new Task[0];
	}	
}

