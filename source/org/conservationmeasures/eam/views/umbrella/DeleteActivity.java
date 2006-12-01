/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public class DeleteActivity extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects() == null)
			return false;
		
		if ((getObjects().length != 1))
			return false;
		
		if (!(getObjects()[0].getType() == ObjectType.TASK))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		try
		{
			Task selectedTask = (Task)getObjects()[0];
			//FIXME we are adding the parentRef during runtime to the tasks.
			//This is done to avoid to having to deal with writing the data
			//migration code. This method call can be eliminated with a data
			//migration.
			possiblySetParentRef(getProject(), selectedTask);
			deleteTasks(getProject(), selectedTask);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private void possiblySetParentRef(Project project, Task selectedTask) throws Exception
	{
		if (selectedTask.getParentRef().getObjectType() != ObjectType.FAKE)
			return;
		
		ChainManager chainManager = new ChainManager(project);
		FactorSet factorSet = chainManager.findFactorsThatHaveThisObject(Strategy.TYPE_INTERVENTION, selectedTask.getId(), Strategy.TAG_ACTIVITY_IDS);
		Factor strategy = (Factor)factorSet.iterator().next();
		selectedTask.setParentRef(new CreateTaskParameter(strategy.getRef()));
	}

	public static void deleteTasks(Project project, Task selectedTask) throws Exception
	{
		createDeleteCommands(project, selectedTask);
		executeDeleteCommands(project);
	}
	
	private static void executeDeleteCommands(Project project) throws ParseException, CommandFailedException
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = (Command[])deleteIdCommandList.toArray(new Command[0]);
			project.executeCommands(commands);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private static void createDeleteCommands(Project project, Task task) throws Exception
	{
		deleteIdCommandList = new Vector();
		
		ORef parentRef = task.getParentRef();
		EAMObject parentObject = project.findObject(parentRef.getObjectType(), parentRef.getObjectId());
		CommandSetObjectData commandSetObjectData;
		if (parentRef.getObjectType() == ObjectType.FACTOR)
			commandSetObjectData = CommandSetObjectData.createRemoveIdCommand(parentObject,	Strategy.TAG_ACTIVITY_IDS, task.getId());		
		else 
			commandSetObjectData = CommandSetObjectData.createRemoveIdCommand(parentObject,	Task.TAG_SUBTASK_IDS, task.getId());

		deleteIdCommandList.add(commandSetObjectData);
		destroyTask(project, task, deleteIdCommandList);
	}

	private static void destroyTask(Project project, Task task, Vector deleteIds) throws Exception
	{
		int subTaskCount = task.getSubtaskCount();
		deleteIds.add(new CommandSetObjectData(task.getType(), task.getId(), Task.TAG_SUBTASK_IDS, ""));
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = task.getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			destroyTask(project, subTask, deleteIds);
		}
		
		deleteIdCommandList.addAll(Arrays.asList(task.createCommandsToClear()));
		deleteIds.add(new CommandDeleteObject(task.getType(), task.getId()));
	}
	
	static Vector deleteIdCommandList;
	WorkPlanView view;
}
