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

public class ActionRedo extends ProjectAction
{
	public ActionRedo(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), "icons/redo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Redo");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Redo last undone action");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		CommandRedo command = new CommandRedo();
		try
		{
			getProject().executeCommand(command);
		}
		catch (NothingToRedoException e)
		{
			// ignore this
		}
	}

	public boolean shouldBeEnabled()
	{
		return (getProject().getIndexToRedo() >= 0);
	}
}
