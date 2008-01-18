/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
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

		undo(getProject());
	}

	public static void undo(Project project) throws CommandFailedException
	{
		try
		{
			EAM.logVerbose("Undo starting");
			Command undone = project.undo();
			if(undone.isEndTransaction())
			{
				while(!undone.isBeginTransaction())
					undone = project.undo();
			}
		}
		catch (NothingToUndoException e)
		{
			// ignore this
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error during undo"), e);
		}
		EAM.logVerbose("Undo finished");
	}

}
