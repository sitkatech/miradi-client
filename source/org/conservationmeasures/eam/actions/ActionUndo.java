/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionUndo extends MainWindowAction
{
	public ActionUndo(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/undo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Undo");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		CommandUndo command = new CommandUndo();
		BaseProject project = getMainWindow().getProject();
		try
		{
			project.executeCommand(command);
		}
		catch(NothingToUndoException e)
		{
			// ignore it
		}
	}
	
	public String getToolTipText()
	{
		return EAM.text("TT|Undo last action");
	}

}
