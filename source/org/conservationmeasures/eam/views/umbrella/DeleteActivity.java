/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
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
		
		if (!(getSelectedObjectType() == ObjectType.TASK))
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		Task selectedTask = (Task)getObjects()[0];
		deleteTask(getProject(), selectedTask);
	}

	public static void deleteTask(Project project, Task selectedTask) throws CommandFailedException
	{
		String[] buttons = {"Delete", "Retain", };
		String[] confirmText = {"Are you sure you want to delete?"};
		if(!EAM.confirmDialog("Delete", confirmText, buttons))
			return;
		
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			//FIXME we are adding the parentRef during runtime to the tasks.
			//This is done to avoid to having to deal with writing the data
			//migration code. This method call can be eliminated with a data
			//migration.
			possiblySetParentRef(project, selectedTask);
			deleteTaskTree(project, selectedTask);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private static void possiblySetParentRef(Project project, Task selectedTask) throws Exception
	{
		if (selectedTask.getParentRef().getObjectType() != ObjectType.FAKE)
			return;
		
		ChainManager chainManager = new ChainManager(project);
		EAMObject foundIn = chainManager.getOwner(selectedTask.getRef());
		selectedTask.setParentRef(foundIn.getRef());
	}

	public static void deleteTaskTree(Project project, Task selectedTask) throws Exception
	{
		Command[] commandToDeleteTasks = createDeleteCommands(project, selectedTask); 
		executeDeleteCommands(project, commandToDeleteTasks);
	}
	
	private static void executeDeleteCommands(Project project, Command[] commands) throws ParseException, CommandFailedException
	{
		project.executeCommands(commands);
	}

	private static Command[] createDeleteCommands(Project project, Task task) throws Exception
	{
		Vector commandsToDeleteTasks = new Vector();
		
		ORef parentRef = task.getParentRef();
		EAMObject parentObject = project.findObject(parentRef.getObjectType(), parentRef.getObjectId());
		CommandSetObjectData commandSetObjectData;
		if (task.isActivity())
			commandSetObjectData = CommandSetObjectData.createRemoveIdCommand(parentObject,	Strategy.TAG_ACTIVITY_IDS, task.getId());
		else if (task.isMethod())
			commandSetObjectData = CommandSetObjectData.createRemoveIdCommand(parentObject,	Indicator.TAG_TASK_IDS, task.getId());
		else 
			commandSetObjectData = CommandSetObjectData.createRemoveIdCommand(parentObject,	Task.TAG_SUBTASK_IDS, task.getId());

		commandsToDeleteTasks.add(commandSetObjectData);
		Vector returnedDeleteTaskCommands = task.getDeleteSelfAndSubtasksCommands(project);
		commandsToDeleteTasks.addAll(returnedDeleteTaskCommands);
		
		return (Command[])commandsToDeleteTasks.toArray(new Command[0]);
	}

	WorkPlanView view;
}
