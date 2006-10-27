/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class ProjectResourcePropertiesPanel extends ObjectPropertiesPanel
{
	public ProjectResourcePropertiesPanel(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
		initializeFields(tags);
	}

	static final String[] tags = new String[] {"Initials", "Name", "Position"};

	public String getPanelDescription()
	{
		return EAM.text("Title|Rescource Properties");
	}

}
