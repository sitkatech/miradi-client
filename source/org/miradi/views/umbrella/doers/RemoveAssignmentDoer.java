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
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

public class RemoveAssignmentDoer extends ObjectsDoer
{
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
			Assignment selectedObject = (Assignment)getObjects()[0];
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

	public static void removeBaseObject(Project project, Assignment assignmentToRemove) throws Exception
	{
		Vector<Command> commands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, assignmentToRemove, BaseObject.TAG_ASSIGNMENT_IDS);
		project.executeCommandsWithoutTransaction(commands);
	}
}
