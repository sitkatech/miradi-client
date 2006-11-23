/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;


import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(Project projectToUse, Actions actions, Task taskToEdit)
	{
		super(projectToUse, taskToEdit.getType(), taskToEdit.getId());
		
		addField(createStringField(Task.TAG_LABEL));
		// FIXME: Add ResourceIds field

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");	
	}
}
