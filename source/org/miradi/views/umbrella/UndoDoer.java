/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.NothingToUndoException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.views.MainWindowDoer;

public class UndoDoer extends MainWindowDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().canUndo());
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;

		undo(getProject());
	}

	public static void undo(Project project) throws CommandFailedException
	{
		try
		{
			project.undo();
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
