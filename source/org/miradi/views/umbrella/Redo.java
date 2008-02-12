/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.commands.Command;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.NothingToRedoException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.views.ProjectDoer;

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
			EAM.logVerbose("Redo starting");
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
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error during redo"), e);
		}
		EAM.logVerbose("Redo finished");
	}
}
