/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class DeleteResource extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProjectResource resource = (ProjectResource)getObjects()[0];
		BaseId idToRemove = resource.getId();
		Vector dialogText = new Vector();
		ORefList allThatUseThisResource = resource.findObjectsThatReferToUs();

		//TODO fix dialog text
		if (allThatUseThisResource.size() > 0)
			dialogText.add("This project resource is being used in the Work Plan and Financial Budget.");
		
		dialogText.add("\nAre you sure you want to delete this resource?");
		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Resource", (String[])dialogText.toArray(new String[0]), buttons))
			return;

		try
		{
			Project project = getProject();
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				project.executeCommandsWithoutTransaction(getClearAssignmentResourcesCommands(allThatUseThisResource));
				int type = resource.getType();
				BaseId id = idToRemove;
				project.executeCommandsWithoutTransaction(resource.createCommandsToClear());
				project.executeCommand(new CommandDeleteObject(type, id));
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

	private Command[] getClearAssignmentResourcesCommands(ORefList allThatUseThisResource) throws CommandFailedException
	{
		Command[] commands = new Command[allThatUseThisResource.size()];
		//TODO: is this assumtion correct, that all resource references are from Assignment objects
		for (int i = 0; i < allThatUseThisResource.size(); i++)
		{
			Assignment assignment = (Assignment) getProject().getObjectManager().findObject(allThatUseThisResource.get(i));
			Command command = new CommandSetObjectData(ObjectType.ASSIGNMENT, assignment.getId(), Assignment.TAG_ASSIGNMENT_RESOURCE_ID, BaseId.INVALID.toString());
			commands[i] = command;
		}
		
		return commands;
	}

}
