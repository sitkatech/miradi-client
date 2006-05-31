/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class TaskPropertiesDialog extends ObjectPropertiesDialog
{

	public TaskPropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit, String[] tags)
	{
		super(parentToUse, objectToEdit);
		initializeFields(tags);
	}

}
