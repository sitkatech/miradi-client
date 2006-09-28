/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;


import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
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
		
		return super.createDialogField(tag);
	}
	
	static final String[] tags = new String[] {Task.TAG_LABEL, Task.TAG_RESOURCE_IDS};

}
