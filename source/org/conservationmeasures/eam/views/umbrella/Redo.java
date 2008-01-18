/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.main.EAM;
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
