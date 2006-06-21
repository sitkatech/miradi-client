/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class TaskPropertiesDialog extends ObjectPropertiesDialog
{

	public TaskPropertiesDialog(MainWindow parentToUse, Task taskToEdit)
	{
		super(parentToUse, taskToEdit);
		task = taskToEdit;
		project = parentToUse.getProject();
		initializeFields(tags);
	}
	
	DialogField createDialogField(String tag)
	{
		if(tag.equals(Task.TAG_RESOURCE_IDS))
			return createResourcePicker(tag);
		
		return super.createDialogField(tag);
	}
	
	DialogField createResourcePicker(String tag)
	{
		String label = EAM.text("Label|" + tag);
		int[] allResourceIds = project.getResourcePool().getIds();
		EAMObject[] availableResources = new EAMObject[allResourceIds.length];
		for(int i = 0; i < availableResources.length; ++i)
			availableResources[i] = project.getResourcePool().find(allResourceIds[i]);

		IdList selectedResources = task.getResourceIdList();
		return new MultiSelectDialogField(tag, label, availableResources, selectedResources);
	}
	
	static final String[] tags = new String[] {Task.TAG_LABEL, Task.TAG_RESOURCE_IDS};


	Project project;
	Task task;
}
