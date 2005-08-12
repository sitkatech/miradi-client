/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

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

}
