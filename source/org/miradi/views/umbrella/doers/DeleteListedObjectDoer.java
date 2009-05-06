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
package org.miradi.views.umbrella.doers;

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public abstract class DeleteListedObjectDoer extends ObjectsDoer
{

	public DeleteListedObjectDoer()
	{
		super();
	}

	public boolean isAvailable()
	{
		if (getObjects().length == 0 )
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
	
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			BaseObject selectedObject = getObjects()[0];
			removeBaseObject(getProject(), selectedObject);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void removeBaseObject(Project project, BaseObject baseObjectToRemove) throws Exception
	{
		Vector<Command> commands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, baseObjectToRemove, getListTag());
		project.executeCommandsWithoutTransaction(commands);
	}

	abstract protected String getListTag();
}