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
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.ProjectMetadata;
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
		Task[] tasksThatUseThisResource = findTasksThatUseThisResource(idToRemove);
		
		Vector dialogText = new Vector();
		
		if(tasksThatUseThisResource.length > 0)
			dialogText.add("This resource is assigned to one or more tasks.");

		if(isTeamMember(idToRemove))
			dialogText.add("This resource is a member of the project team.");

		dialogText.add("\nAre you sure you want to delete this resource?");

		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Resource", (String[])dialogText.toArray(new String[0]), buttons))
			return;

		
		
		try
		{
			Command[] removeFromTasks = createCommandsToRemoveResourceFromTasks(idToRemove, tasksThatUseThisResource);
			Command[] removeFromTeam = createCommandsToRemoveResourceFromTeam(idToRemove);
			
			getProject().executeCommand(new CommandBeginTransaction());
			for(int i = 0; i < removeFromTasks.length; ++i)
				getProject().executeCommand(removeFromTasks[i]);
			for(int i = 0; i < removeFromTeam.length; ++i)
				getProject().executeCommand(removeFromTeam[i]);
			int type = resource.getType();
			BaseId id = idToRemove;
			getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, EAMBaseObject.DEFAULT_LABEL));
			getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_INITIALS, ""));
			getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_NAME, ""));
			getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_POSITION, ""));
			getProject().executeCommand(new CommandDeleteObject(type, id));
			getProject().executeCommand(new CommandEndTransaction());
		}
		catch(CommandFailedException e)
		{
			throw(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		}
	}

	private boolean isTeamMember(BaseId idToRemove)
	{
		return getProject().getMetadata().getTeamResourceIdList().contains(idToRemove);
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
	
	private Command[] createCommandsToRemoveResourceFromTeam(BaseId idToRemove) throws Exception
	{
		if(!isTeamMember(idToRemove))
			return new Command[0];
		ProjectMetadata metadata = getProject().getMetadata();
		Command cmd = CommandSetObjectData.createRemoveIdCommand(metadata, metadata.TAG_TEAM_RESOURCE_IDS, idToRemove);
		return new Command[] {cmd};
	}
	
	Task[] findTasksThatUseThisResource(BaseId resourceId)
	{
		Vector foundTasks = new Vector();
		TaskPool pool = getProject().getTaskPool();
		BaseId[] allTaskIds = pool.getIds();
		for(int i = 0; i < allTaskIds.length; ++i)
		{
			Task task = pool.find(allTaskIds[i]);
			if(task.getResourceIdList().contains(resourceId))
				foundTasks.add(task);
		}
		
		return (Task[])foundTasks.toArray(new Task[0]);
	}
}
