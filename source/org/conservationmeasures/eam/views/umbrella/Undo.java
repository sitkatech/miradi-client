/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Undo extends ProjectDoer
{
	public boolean isAvailable()
	{
		return (getProject().getIndexToUndo() >= 0);
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			boolean stillMoreTransactionsToDo = getProject().getCommandToUndo().isEndTransaction();
			do
			{
				if(getProject().getCommandToUndo().isBeginTransaction())
					stillMoreTransactionsToDo = false;
				getProject().executeCommand(new CommandUndo());
			}while(stillMoreTransactionsToDo);
		}
		catch (NothingToUndoException e)
		{
			// ignore this
		}
	}

}
