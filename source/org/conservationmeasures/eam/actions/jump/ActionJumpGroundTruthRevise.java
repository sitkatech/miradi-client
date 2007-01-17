/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpGroundTruthRevise extends MainWindowAction
{
	public ActionJumpGroundTruthRevise(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	static String getLabel()
	{
		return EAM.text("Ground Truth and Revise Model");
	}
	
}
