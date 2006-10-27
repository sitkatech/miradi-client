/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class ObjectivePropertiesPanel extends DesirePropertiesPanel
{
	public ObjectivePropertiesPanel(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Objective Properties"); 
	}

}
