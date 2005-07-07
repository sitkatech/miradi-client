/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionContextualHelp extends MainWindowAction
{
	public ActionContextualHelp(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|ContextSensitive|Help");
	}

	public void actionPerformed(ActionEvent arg0)
	{
		EAM.logWarning("Context-sensitive help action invoked!");
	}
}