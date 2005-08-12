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

public class ActionUndo extends ProjectAction
{
	public ActionUndo(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), "icons/undo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Undo");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Undo last action");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		CommandUndo command = new CommandUndo();
		try
		{
			getProject().executeCommand(command);
		}
		catch(NothingToUndoException e)
		{
			// ignore it
		}
	}
	
	public boolean shouldBeEnabled()
	{
		return (getProject().getIndexToUndo() >= 0);
	}
	
}
