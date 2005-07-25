/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionUndo extends MainWindowAction
{
	public ActionUndo(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Undo");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		CommandUndo command = new CommandUndo();
		BaseProject project = getMainWindow().getProject();
		project.executeCommand(command);
	}

}
