/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.text.ParseException;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

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
		deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedTask);
	}

	public static void deleteTaskWithUserConfirmation(Project project, ORefList possibleParents, Task selectedTask) throws CommandFailedException
	{
		Vector dialogText = new Vector();
		boolean containsMoreThanOneParent = possibleParents.getOverlappingRefs(selectedTask.findObjectsThatReferToUs()).size() > 1;
		if (containsMoreThanOneParent)
			dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));
		
		dialogText.add(EAM.text("All subtasks within this item will be removed as well."));
		String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
		if(!EAM.confirmDialog(EAM.text("Title|Delete"), (String[]) dialogText.toArray(new String[0]), buttons))
			return;
		
		deleteTask(project, possibleParents, selectedTask);
	}
	
	private static void deleteTask(Project project, ORefList possibleParents, Task selectedTask) throws CommandFailedException
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			deleteTaskTree(project, possibleParents, selectedTask);
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

	public static void deleteTaskTree(Project project, ORefList possibleParents, Task selectedTask) throws Exception
	{
		Vector commandToDeleteTasks = createDeleteCommands(project, possibleParents, selectedTask); 
		executeDeleteCommands(project, commandToDeleteTasks);
		
		if (! selectedTask.isOrphandTask())
			return;
		
		Vector commandsToDeletTask = selectedTask.getDeleteSelfAndSubtasksCommands(project);
		project.executeCommandsWithoutTransaction(commandsToDeletTask);
	}
	
	private static void executeDeleteCommands(Project project, Vector commands) throws ParseException, CommandFailedException
	{
		project.executeCommandsWithoutTransaction(commands);
	}

	private static Vector createDeleteCommands(Project project, ORefList possibleParents, Task task) throws Exception
	{
		
		//FIXME need to consider parent hierachy when creating commands.  first refactor dup code.  
		Vector commandsToDeleteTasks = new Vector();
		commandsToDeleteTasks.addAll(buildRemoveCommandsForActivityIds(project, possibleParents, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForMethodIds(project, possibleParents, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForTaskIds(project, task));
		
		return commandsToDeleteTasks;
	}
	
	private static Vector buildRemoveCommandsForActivityIds(Project project, ORefList possibleParents, Task task) throws Exception
	{
		if (! task.isActivity())
			return new Vector();
		
		return buildRemoveCommands(project, Strategy.getObjectType(), possibleParents, Strategy.TAG_ACTIVITY_IDS, task);
	}
	
	private static Vector buildRemoveCommandsForMethodIds(Project project, ORefList selectionHierarchy, Task task) throws Exception
	{
		if (! task.isMethod())
			return new Vector();
		
		return buildRemoveCommands(project, Indicator.getObjectType(), selectionHierarchy, Indicator.TAG_TASK_IDS, task);
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
	
	private static Vector buildRemoveCommands(Project project, int parentType, ORefList possibleParents, String tag, Task task) throws Exception
	{
		Vector removeCommands = new Vector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(parentType);
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrer = project.findObject(referrerRefs.get(i));
			if (possibleParents.contains(referrer.getRef()))
				removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, task.getId()));
		}
		
		return removeCommands;		
	}
}
