/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;

public abstract class DeleteAnnotationDoer extends ObjectsDoer
{
	abstract String[] getDialogText();
	abstract String getAnnotationIdListTag();

	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		String tag = getAnnotationIdListTag();
		String[] dialogText = getDialogText();
		EAMBaseObject annotationToDelete = (EAMBaseObject)getObjects()[0];
		Factor selectedFactor = getSelectedFactor();
		
		deleteAnnotationViaCommands(getProject(), selectedFactor, annotationToDelete, tag, dialogText);
	}

	public static void deleteAnnotationViaCommands(Project project, Factor factor, EAMBaseObject annotationToDelete, String annotationIdListTag, String[] confirmDialogText) throws CommandFailedException
	{
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete", confirmDialogText, buttons))
			return;
	
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = buildCommandsToDeleteAnnotation(project, factor, annotationIdListTag, annotationToDelete);
			for(int i = 0; i < commands.length; ++i)
				project.executeCommand(commands[i]);
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
	
	public static Command[] buildCommandsToDeleteAnnotation(Project project, Factor factor, String annotationIdListTag, EAMBaseObject annotationToDelete) throws CommandFailedException, ParseException, Exception
	{
		Vector commands = new Vector();
	
		int type = annotationToDelete.getType();
		BaseId idToRemove = annotationToDelete.getId();
		commands.add(CommandSetObjectData.createRemoveIdCommand(factor, annotationIdListTag, idToRemove));
		FactorSet nodesThatUseThisAnnotation = new ChainManager(project).findFactorsThatUseThisAnnotation(type, idToRemove);
		if(nodesThatUseThisAnnotation.size() == 1)
		{
			commands.addAll(buildCommandsToDeleteSubTasks(project, type, idToRemove));
			commands.addAll(Arrays.asList(annotationToDelete.createCommandsToClear()));
			commands.add(new CommandDeleteObject(type, idToRemove));
		}
		
		return (Command[])commands.toArray(new Command[0]);
	}

	public Factor getSelectedFactor()
	{
		EAMObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != ObjectType.FACTOR)
			return null;
		
		return (Factor)selected;
	}
	
	private static Vector buildCommandsToDeleteSubTasks(Project project, int type, BaseId id) throws Exception
	{
		Vector commands = new Vector();
		if (!(type == ObjectType.INDICATOR))
			return commands;
	
		Indicator indicator = (Indicator)project.findObject(type, id);
		IdList subtaskList = indicator.getSubtaskIdList();
		for (int i  = 0; i < subtaskList.size(); i++)
		{
			Task taskToDelete = (Task)project.findObject(ObjectType.TASK, subtaskList.get(i));
			destroyTask(project, taskToDelete, commands);
		}
		
		return commands;
	}
	
	private static void destroyTask(Project project, Task task, Vector deleteIds) throws Exception
	{
		deleteIds.add(new CommandSetObjectData(task.getType(), task.getId(), Task.TAG_SUBTASK_IDS, ""));
		int subTaskCount = task.getSubtaskCount();
		for (int index = 0; index < subTaskCount; index++)
		{
			BaseId subTaskId = task.getSubtaskId(index);
			Task  subTask = (Task)project.findObject(ObjectType.TASK, subTaskId);
			destroyTask(project, subTask, deleteIds);
		}
		
		deleteIds.addAll(Arrays.asList(task.createCommandsToClear()));
		deleteIds.add(new CommandDeleteObject(task.getType(), task.getId()));
	}
}
