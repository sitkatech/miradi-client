/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;

import java.text.ParseException;
import java.util.Vector;

public class DeleteActivityDoer extends ObjectsDoer
{
	@Override
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

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;
	
		Task selectedTask = (Task)getObjects()[0];
		deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedTask);
	}

	public static void deleteTaskWithUserConfirmation(Project project, ORefList selectionHierarchy, Task selectedTask) throws CommandFailedException
	{
		Vector<String> dialogText = new Vector<String>();
		boolean containsMoreThanOneParent = selectionHierarchy.getOverlappingRefs(selectedTask.findAllObjectsThatReferToUs()).size() > 1;
		if (containsMoreThanOneParent)
			dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));
		
		dialogText.add(EAM.text("All subtasks within this item will be removed as well."));
		String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
		if(!EAM.confirmDialog(EAM.text("Title|Delete"), dialogText.toArray(new String[0]), buttons))
			return;
		
		deleteTask(project, selectionHierarchy, selectedTask);
	}
	
	private static void deleteTask(Project project, ORefList selectionHierarchy, Task selectedTask) throws CommandFailedException
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			deleteTaskTree(project, selectionHierarchy, selectedTask);
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

	public static void deleteTaskTree(Project project, ORefList selectionHierarchy, Task selectedTask) throws Exception
	{
		CommandVector commandToDeleteTasks = createDeleteCommands(project, selectionHierarchy, selectedTask);
		executeDeleteCommands(project, commandToDeleteTasks);
		
		if (! selectedTask.isOrphanedTask())
			return;
		
		CommandVector commandsToDeleteTask = selectedTask.createCommandsToDeleteChildrenAndObject();
		project.executeCommands(commandsToDeleteTask);
	}
	
	private static void executeDeleteCommands(Project project, CommandVector commands) throws ParseException, CommandFailedException
	{
		project.executeCommands(commands);
	}

	private static CommandVector createDeleteCommands(Project project, ORefList selectionHierarchy, Task task) throws Exception
	{
		
		//FIXME medium: need to consider parent hierarchy when creating commands.  First refactor duplicated code.  
		CommandVector commandsToDeleteTasks = new CommandVector();
		commandsToDeleteTasks.addAll(buildDeleteDiagramFactors(project, selectionHierarchy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForActivityIds(project, selectionHierarchy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForTaskIds(project, task));

		return commandsToDeleteTasks;
	}
	
	private static CommandVector buildRemoveCommandsForActivityIds(Project project, ORefList selectionHierarchy, Task task) throws Exception
	{
		if (! task.isActivity())
			return new CommandVector();
		
		return buildRemoveCommands(project, StrategySchema.getObjectType(), selectionHierarchy, Strategy.TAG_ACTIVITY_IDS, task);
	}
	
	private static CommandVector buildRemoveCommandsForTaskIds(Project project, Task task) throws ParseException
	{
		if (! task.isTask())
			return new CommandVector();
		
		CommandVector removeCommands = new CommandVector();
		BaseObject parentObject = task.getOwner();
		removeCommands.add(CommandSetObjectData.createRemoveIdCommand(parentObject,	Task.TAG_SUBTASK_IDS, task.getId()));
		
		return removeCommands;
	}
	
	private static CommandVector buildDeleteDiagramFactors(Project project, ORefList selectionHierarchy, Task task) throws Exception
	{
		CommandVector commands = new CommandVector();
		if (!task.isActivity())
			return commands;
		
		ORef strategyRef = selectionHierarchy.getRefForType(StrategySchema.getObjectType());
		ORefList activityDiagramFactorReferrerRefs = task.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());
				
		for (int index = 0; index < activityDiagramFactorReferrerRefs.size(); ++index)
		{
			ORef activityDiagramFactorRef = activityDiagramFactorReferrerRefs.get(index);
			DiagramFactor activityDiagramFactor = DiagramFactor.find(project, activityDiagramFactorRef);
			ORefList diagramObjectsWithActivities = activityDiagramFactor.findObjectsThatReferToUs(ResultsChainDiagramSchema.getObjectType());
			for (int diagramObjectIndex = 0; diagramObjectIndex < diagramObjectsWithActivities.size(); ++diagramObjectIndex)
			{
				DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectsWithActivities.get(diagramObjectIndex));
				ORefList strategyRefs = getDiagramObjectStrategies(project, diagramObject);
				if (strategyRefs.contains(strategyRef))
				{
					commands.add(CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, activityDiagramFactorRef.getObjectId()));
					commands.addAll(activityDiagramFactor.createCommandsToDeleteChildrenAndObject());
				}
			}
		}
			
		return commands;
	}
	
	private static ORefList getDiagramObjectStrategies(Project project, DiagramObject diagramObject)
	{
		ORefList strategyRefs = new ORefList();
		ORefList diagramFactorRefs =  diagramObject.getAllDiagramFactorRefs();
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			if (Strategy.is(diagramFactor.getWrappedType()))
				strategyRefs.add(diagramFactor.getWrappedORef());
		}
		
		return strategyRefs;
	}
	
	private static CommandVector buildRemoveCommands(Project project, int parentType, ORefList selectionHierarchy, String tag, Task task) throws Exception
	{
		CommandVector removeCommands = new CommandVector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(parentType);
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrer = project.findObject(referrerRefs.get(i));
			if (selectionHierarchy.contains(referrer.getRef()))
				removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, task.getId()));
		}
		
		return removeCommands;		
	}
}
