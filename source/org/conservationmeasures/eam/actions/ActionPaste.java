/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionPaste extends MainWindowAction
{
	public ActionPaste(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/paste.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Paste");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		// TODO Auto-generated method stub

	}

}
