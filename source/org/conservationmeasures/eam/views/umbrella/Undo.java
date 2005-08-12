/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Undo extends ProjectDoer
{
	public Undo(BaseProject project)
	{
		super(project);
	}

	public boolean isAvailable()
	{
		return (getProject().getIndexToUndo() >= 0);
	}

	public void doIt() throws CommandFailedException
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

}
