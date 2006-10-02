/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Undo extends ProjectDoer
{
	public boolean isAvailable()
	{
		return (getProject().canUndo());
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			Command undone = getProject().undo();
			if(undone.isEndTransaction())
			{
				while(!undone.isBeginTransaction())
					undone = getProject().undo();
			}
		}
		catch (NothingToUndoException e)
		{
			// ignore this
		}
	}

}
