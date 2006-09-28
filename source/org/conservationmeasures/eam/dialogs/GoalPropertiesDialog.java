/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;

public class GoalPropertiesDialog extends DesirePropertiesDialog
{
	public GoalPropertiesDialog(MainWindow parentToUse, EAMObject goalToEdit) throws Exception
	{
		super(parentToUse, goalToEdit, EAM.text("Title|Goal Properties"));
	}
	
}
