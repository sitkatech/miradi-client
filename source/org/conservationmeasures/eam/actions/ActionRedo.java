/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandRedo;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionRedo extends MainWindowAction
{
	public ActionRedo(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Redo");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		CommandRedo command = new CommandRedo();
		BaseProject project = getMainWindow().getProject();
		try
		{
			project.executeCommand(command);
		}
		catch (NothingToRedoException e)
		{
			// ignore this
		}
	}
}
