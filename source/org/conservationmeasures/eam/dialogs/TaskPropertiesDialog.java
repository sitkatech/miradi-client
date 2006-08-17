/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.Task;

public class TaskPropertiesDialog extends ObjectPropertiesDialog
{

	public TaskPropertiesDialog(MainWindow parentToUse, EAMObject taskToEdit) throws Exception
	{
		super(parentToUse, taskToEdit);
		setTitle(EAM.text("Title|Activity Properties"));
		initializeFields(tags);
	}
	
	DialogField createDialogField(String tag) throws Exception
	{
		if(tag.equals(Task.TAG_RESOURCE_IDS))
			return createResourcePicker(tag);
		
		return super.createDialogField(tag);
	}
	
	DialogField createResourcePicker(String tag) throws ParseException
	{
		String label = EAM.text("Label|" + tag);
		BaseId[] allResourceIds = getProject().getResourcePool().getIds();
		EAMObject[] availableResources = new EAMObject[allResourceIds.length];
		for(int i = 0; i < availableResources.length; ++i)
			availableResources[i] = getProject().getResourcePool().find(allResourceIds[i]);

		int type = getObject().getType();
		BaseId id = getObject().getId();
		IdList selectedResources = new IdList(getProject().getObjectData(type, id, Task.TAG_RESOURCE_IDS));
		return new MultiSelectDialogField(tag, label, availableResources, selectedResources);
	}
	
	static final String[] tags = new String[] {Task.TAG_LABEL, Task.TAG_RESOURCE_IDS};

}
