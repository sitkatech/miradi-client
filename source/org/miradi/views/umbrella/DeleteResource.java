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

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class DeleteResource extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProjectResource resource = (ProjectResource)getObjects()[0];
		Vector dialogText = new Vector();
		ORefList allThatUseThisResource = resource.findObjectsThatReferToUs();

		//TODO fix dialog text
		if (allThatUseThisResource.size() > 0)
			dialogText.add(EAM.text("This project resource is being used in the Work Plan and Financial Budget."));
		
		dialogText.add(EAM.text("\nAre you sure you want to delete this resource?"));
		String[] buttons = {EAM.text("Yes"), EAM.text("No"), };
		if(!EAM.confirmDialog(EAM.text("Delete Resource"), (String[])dialogText.toArray(new String[0]), buttons))
			return;

		try
		{
			Project project = getProject();
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				project.executeCommandsWithoutTransaction(createCommandsToRemoveFromReferrers(allThatUseThisResource, resource.getRef()));
				project.executeCommandsWithoutTransaction(resource.createCommandsToClear());
				project.executeCommand(new CommandDeleteObject(resource.getRef()));
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}
		catch(CommandFailedException e)
		{
			throw(e);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private Command[] createCommandsToRemoveFromReferrers(ORefList allThatUseThisResource, ORef resourceRef) throws Exception
	{
		Vector<Command> commands = new Vector<Command>();
		for (int i = 0; i < allThatUseThisResource.size(); ++i)
		{
			ORef referrerRef = allThatUseThisResource.get(i);
			commands.addAll(removeFromAssignment(referrerRef));
			commands.addAll(createCommandToRemoveFromRefList(resourceRef, referrerRef, BaseObject.TAG_WHO_OVERRIDE_REFS));
		}
		
		return commands.toArray(new Command[0]);
	}

	private Vector<Command> createCommandToRemoveFromRefList(ORef resourceRef, ORef referrerRef, String tagToRemoveFrom) throws Exception
	{
		Vector<Command> commands = new Vector<Command>();
		BaseObject foundObject = getProject().findObject(referrerRef);
		commands.add(CommandSetObjectData.createRemoveORefCommand(foundObject, tagToRemoveFrom, resourceRef));
		
		return commands;
	}

	private Vector<Command> removeFromAssignment(ORef ref)
	{
		Vector<Command> commands = new Vector<Command>();
		if (ResourceAssignment.is(ref))
			commands.add(new CommandSetObjectData(ref, ResourceAssignment.TAG_ASSIGNMENT_RESOURCE_ID, BaseId.INVALID.toString()));
		
		return commands;
	}
}
