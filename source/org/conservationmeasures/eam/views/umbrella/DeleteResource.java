/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
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
		Task[] tasksThatUseThisResource = getProject().findTasksThatUseThisResource(idToRemove);
		
		Vector dialogText = new Vector();
		
		if(tasksThatUseThisResource.length > 0)
			dialogText.add("This resource is assigned to one or more tasks.");

		dialogText.add("\nAre you sure you want to delete this resource?");

		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Resource", (String[])dialogText.toArray(new String[0]), buttons))
			return;

		try
		{
			Command[] removeFromTasks = createCommandsToRemoveResourceFromTasks(idToRemove, tasksThatUseThisResource);
			
			getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				for(int i = 0; i < removeFromTasks.length; ++i)
					getProject().executeCommand(removeFromTasks[i]);
				int type = resource.getType();
				BaseId id = idToRemove;
				getProject().executeCommands(resource.createCommandsToClear());
				getProject().executeCommand(new CommandDeleteObject(type, id));
			}
			finally
			{
				getProject().executeCommand(new CommandEndTransaction());
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

	private Command[] createCommandsToRemoveResourceFromTasks(BaseId idToRemove, Task[] tasksThatUseThisResource) throws Exception
	{
		Command[] removeFromTasks = new Command[tasksThatUseThisResource.length];
		for(int i = 0; i < removeFromTasks.length; ++i)
		{
			String tag = Task.TAG_RESOURCE_IDS;
			Task task = tasksThatUseThisResource[i];
			removeFromTasks[i] = CommandSetObjectData.createRemoveIdCommand(task, tag, idToRemove);
		}
		return removeFromTasks;
	}
}
