/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class IndicatorPropertiesDialog extends ObjectPropertiesDialog
{
	public IndicatorPropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit)
	{
		super(parentToUse, objectToEdit);
		initializeFields(tags);
	}

	static final String[] tags = new String[] {"Identifier", "Label"};

}
