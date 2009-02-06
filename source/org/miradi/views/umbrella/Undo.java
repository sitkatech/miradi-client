/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.commands.Command;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.NothingToUndoException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.views.ProjectDoer;

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
