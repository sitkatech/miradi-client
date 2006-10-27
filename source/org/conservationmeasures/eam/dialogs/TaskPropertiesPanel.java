/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;


import org.conservationmeasures.eam.dialogfields.DialogField;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;

public class TaskPropertiesPanel extends ObjectPropertiesPanel
{

	public TaskPropertiesPanel(MainWindow parentToUse, EAMObject taskToEdit) throws Exception
	{
		super(parentToUse, taskToEdit);
		initializeFields(tags);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");	
	}
	
	DialogField createDialogField(String tag, String existingValue) throws Exception
	{
		
		return super.createDialogField(tag, existingValue);
	}
	
	static final String[] tags = new String[] {Task.TAG_LABEL, Task.TAG_RESOURCE_IDS};

}
