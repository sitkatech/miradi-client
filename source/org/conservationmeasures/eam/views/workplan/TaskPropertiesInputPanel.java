/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class TaskPropertiesInputPanel extends ObjectDataInputPanel
{
	
	public TaskPropertiesInputPanel(Project projectToUse, Actions actions, BaseId idToEdit, AssignmentEditorComponent editorComponentToUse) throws Exception
	{
		this(projectToUse, actions, idToEdit);
		editorComponent = editorComponentToUse;
	}
	
	public TaskPropertiesInputPanel(Project projectToUse, Actions actions, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		
		addCommonFields();
	}
	
	public void dispose()
	{
		super.dispose();
	}

	private void addCommonFields()
	{
		addField(createStringField(ObjectType.TASK, Task.TAG_LABEL));
		updateFieldsFromProject();
	}
	
	//TODO: THis override is here because multiple task in a path can not be disnguished between methods and task or other sub types
	public void setObjectId(Vector orefs)
	{
		if (orefs.size()>1) 
		{
			BaseId id = ((ORef)orefs.get(orefs.size()-1)).getObjectId();
			setObjectId(id);
		}
		else
			super.setObjectId(orefs);
	}
	
	public void setObjectId(BaseId id)
	{
		if (editorComponent == null)
			return;
	
		editorComponent.setTaskId(id);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void updateTable()
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
		
	Project project;
	AssignmentEditorComponent editorComponent;
	
}