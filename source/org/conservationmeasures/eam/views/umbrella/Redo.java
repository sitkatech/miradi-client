/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Redo extends ProjectDoer
{
	public boolean isAvailable()
	{
		return (getProject().canRedo());
	}

	public void doIt() throws CommandFailedException
	{
		redo(getProject());
	}

	public static void redo(Project project) throws CommandFailedException
	{
		try
		{
			Command redone = project.redo();
			if(redone.isBeginTransaction())
			{
				while(!redone.isEndTransaction())
					redone = project.redo();
			}
		}
		catch (NothingToRedoException e)
		{
			// ignore this
		}
	}
}
