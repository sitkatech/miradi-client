/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Objective;

abstract public class DesirePropertiesPanel extends ObjectPropertiesPanel
{
	public DesirePropertiesPanel(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
		//super(parentToUse, objectToEdit);
		//setTitle(title);
		initializeFields(tags);
	}
	abstract public String getPanelDescription();
	
	static final String[] tags = new String[] {
		Objective.TAG_SHORT_LABEL, 
		Objective.TAG_LABEL,
		Objective.TAG_FULL_TEXT,
		};

}
