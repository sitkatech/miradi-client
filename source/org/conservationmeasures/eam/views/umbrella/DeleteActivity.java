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
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

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
		String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
		String[] confirmText = {EAM.text("This will delete any subtasks too. Are you sure you want to delete?")};
		if(!EAM.confirmDialog(EAM.text("Title|Delete"), confirmText, buttons))
			return;
		
		project.executeCommand(new CommandBeginTransaction());
		try
		{
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

	public static void deleteTaskTree(Project project, Task selectedTask) throws Exception
	{
		Command[] commandToDeleteTasks = createDeleteCommands(project, selectedTask); 
		executeDeleteCommands(project, commandToDeleteTasks);
	}
	
	private static void executeDeleteCommands(Project project, Command[] commands) throws ParseException, CommandFailedException
	{
		project.executeCommandsWithoutTransaction(commands);
	}

	private static Command[] createDeleteCommands(Project project, Task task) throws Exception
	{
		Vector commandsToDeleteTasks = new Vector();
		commandsToDeleteTasks.addAll(buildRemoveCommandsForActivityIds(project, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForMethodIds(project, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForTaskIds(project, task));
		commandsToDeleteTasks.addAll(task.getDeleteSelfAndSubtasksCommands(project));
		
		return (Command[])commandsToDeleteTasks.toArray(new Command[0]);
	}
	
	private static Vector buildRemoveCommandsForActivityIds(Project project, Task task) throws ParseException
	{
		if (! task.isActivity())
			return new Vector();
		
		Vector removeCommands = new Vector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(Strategy.getObjectType());
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrrer = project.findObject(referrerRefs.get(i));
			removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrrer, Strategy.TAG_ACTIVITY_IDS, task.getId()));
		}
		
		return removeCommands;
	}
	
	private static Vector buildRemoveCommandsForMethodIds(Project project, Task task) throws ParseException
	{
		if (! task.isMethod())
			return new Vector();
		
		Vector removeCommands = new Vector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(Indicator.getObjectType());
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrrer = project.findObject(referrerRefs.get(i));
			removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrrer, Indicator.TAG_TASK_IDS, task.getId()));
		}
		
		return removeCommands;
	}
	
	private static Vector buildRemoveCommandsForTaskIds(Project project, Task task) throws ParseException
	{
		if (! task.isTask())
			return new Vector();
		
		Vector removeCommands = new Vector();
		BaseObject parentObject = task.getOwner();
		removeCommands.add(CommandSetObjectData.createRemoveIdCommand(parentObject,	Task.TAG_SUBTASK_IDS, task.getId()));
		
		return removeCommands;
	}
}
