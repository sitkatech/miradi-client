/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Objective;

public class DesirePropertiesDialog extends ObjectPropertiesDialog
{
	public DesirePropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit, String title) throws Exception
	{
		super(parentToUse, objectToEdit);
		setTitle(EAM.text("Title|Goal Properties"));
		initializeFields(tags);
	}

	static final String[] tags = new String[] {
		Objective.TAG_SHORT_LABEL, 
		Objective.TAG_LABEL,
		Objective.TAG_FULL_TEXT,
		};

}
