/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class ProjectResourcePropertiesDialog extends ObjectPropertiesDialog
{
	public ProjectResourcePropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
		setTitle("Title|Rescource Properties");
		initializeFields(tags);
	}

	static final String[] tags = new String[] {"Initials", "Name", "Position"};

}
