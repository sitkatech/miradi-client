/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

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
