/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.TaskPool;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteResource extends ViewDoer
{
	public ResourceManagementPanel getResourcePanel()
	{
		return ((StrategicPlanView)getView()).getResourcePanel();
	}
	
	public boolean isAvailable()
	{
		if(getResourcePanel() == null)
			return false;
		
		return getResourcePanel().getSelectedResource() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProjectResource resource = getResourcePanel().getSelectedResource();
		
		BaseId idToRemove = resource.getId();
		Task[] tasksThatUseThisResource = findTasksThatUseThisResource(idToRemove);
		if(tasksThatUseThisResource.length > 0)
		{
			String[] dialogText = {
					"This resource is assigned to one or more tasks.", 
					"Are you sure you want to delete it?", 
					};
			String[] buttons = {"Yes", "No", };
			if(!EAM.confirmDialog("Delete Resource", dialogText, buttons))
				return;
		}
		
		Command[] removeFromTasks = createCommandsToRemoveResourceFromTasks(idToRemove, tasksThatUseThisResource);
		
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < removeFromTasks.length; ++i)
		{
			getProject().executeCommand(removeFromTasks[i]);
		}
		int type = resource.getType();
		BaseId id = idToRemove;
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, EAMBaseObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_INITIALS, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_NAME, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, ProjectResource.TAG_POSITION, ""));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		getProject().executeCommand(new CommandEndTransaction());
	}

	private Command[] createCommandsToRemoveResourceFromTasks(BaseId idToRemove, Task[] tasksThatUseThisResource) throws CommandFailedException
	{
		Command[] removeFromTasks = new Command[tasksThatUseThisResource.length];
		try
		{
			for(int i = 0; i < removeFromTasks.length; ++i)
			{
				String tag = Task.TAG_RESOURCE_IDS;
				Task task = tasksThatUseThisResource[i];
				removeFromTasks[i] = CommandSetObjectData.createRemoveIdCommand(task, tag, idToRemove);
			}
		}
		catch (ParseException e)
		{
			throw new CommandFailedException(e);
		}
		return removeFromTasks;
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
