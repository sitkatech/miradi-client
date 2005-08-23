/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandRedo;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Redo extends ProjectDoer
{
	public Redo(BaseProject project)
	{
		super(project);
	}

	public boolean isAvailable()
	{
		return (getProject().getIndexToRedo() >= 0);
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			boolean stillMoreTransactionsToDo = false;
			do
			{
				Command commandToRedo = getProject().getCommandToRedo();
				if(commandToRedo.isEndTransaction())
					stillMoreTransactionsToDo = false;
				if(commandToRedo.isBeginTransaction())
					stillMoreTransactionsToDo = true;
				getProject().executeCommand(new CommandRedo());
				// TODO: should we fire a command-undone here?
			}while(stillMoreTransactionsToDo);
		}
		catch (NothingToRedoException e)
		{
			// ignore this
		}
	}
}
