/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.CommandVector;
import org.miradi.views.ObjectsDoer;

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
	public void doIt() throws Exception
	{
		if(!isAvailable())
			return;
	
		Task selectedTask = (Task)getObjects()[0];
		deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedTask);
	}

	public static void deleteTaskWithUserConfirmation(Project project, ORefList selectionHierachy, Task selectedTask) throws CommandFailedException
	{
		Vector<String> dialogText = new Vector<String>();
		boolean containsMoreThanOneParent = selectionHierachy.getOverlappingRefs(selectedTask.findObjectsThatReferToUs()).size() > 1;
		if (containsMoreThanOneParent)
			dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));
		
		dialogText.add(EAM.text("All subtasks within this item will be removed as well."));
		String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
		if(!EAM.confirmDialog(EAM.text("Title|Delete"), dialogText.toArray(new String[0]), buttons))
			return;
		
		deleteTask(project, selectionHierachy, selectedTask);
	}
	
	private static void deleteTask(Project project, ORefList selectionHierachy, Task selectedTask) throws CommandFailedException
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			deleteTaskTree(project, selectionHierachy, selectedTask);
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

	public static void deleteTaskTree(Project project, ORefList selectionHierachy, Task selectedTask) throws Exception
	{
		CommandVector commandToDeleteTasks = createDeleteCommands(project, selectionHierachy, selectedTask); 
		executeDeleteCommands(project, commandToDeleteTasks);
		
		if (! selectedTask.isOrphandTask())
			return;
		
		CommandVector commandsToDeletTask = selectedTask.createCommandsToDeleteChildrenAndObject();
		project.executeCommandsWithoutTransaction(commandsToDeletTask);
	}
	
	private static void executeDeleteCommands(Project project, CommandVector commands) throws ParseException, CommandFailedException
	{
		project.executeCommandsWithoutTransaction(commands);
	}

	private static CommandVector createDeleteCommands(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		
		//FIXME medium: need to consider parent hierachy when creating commands.  first refactor dup code.  
		CommandVector commandsToDeleteTasks = new CommandVector();
		commandsToDeleteTasks.addAll(buildDeleteDiagramFactors(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForActivityIds(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForMethodIds(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForTaskIds(project, task));
		
		return commandsToDeleteTasks;
	}
	
	private static CommandVector buildRemoveCommandsForActivityIds(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		if (! task.isActivity())
			return new CommandVector();
		
		return buildRemoveCommands(project, Strategy.getObjectType(), selectionHierachy, Strategy.TAG_ACTIVITY_IDS, task);
	}
	
	private static CommandVector buildRemoveCommandsForMethodIds(Project project, ORefList selectionHierarchy, Task task) throws Exception
	{
		if (! task.isMethod())
			return new CommandVector();
		
		return buildRemoveCommands(project, Indicator.getObjectType(), selectionHierarchy, Indicator.TAG_METHOD_IDS, task);
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
	
	private static CommandVector buildDeleteDiagramFactors(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		CommandVector commands = new CommandVector();
		if (!task.isActivity())
			return commands;
		
		ORef strategyRef = selectionHierachy.getRefForType(Strategy.getObjectType());
		ORefList activityDiagramFactorReferrerRefs = task.findObjectsThatReferToUs(DiagramFactor.getObjectType());
				
		for (int index = 0; index < activityDiagramFactorReferrerRefs.size(); ++index)
		{
			ORef activityDiagramFactorRef = activityDiagramFactorReferrerRefs.get(index);
			DiagramFactor activityDiagramFactor = DiagramFactor.find(project, activityDiagramFactorRef);
			ORefList diagramObjectsWithAcitivities = activityDiagramFactor.findObjectsThatReferToUs(ResultsChainDiagram.getObjectType());
			for (int diagramObjectIndex = 0; diagramObjectIndex < diagramObjectsWithAcitivities.size(); ++diagramObjectIndex)
			{
				DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectsWithAcitivities.get(diagramObjectIndex));
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
	
	private static CommandVector buildRemoveCommands(Project project, int parentType, ORefList selectionHierachy, String tag, Task task) throws Exception
	{
		CommandVector removeCommands = new CommandVector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(parentType);
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrer = project.findObject(referrerRefs.get(i));
			if (selectionHierachy.contains(referrer.getRef()))
				removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, task.getId()));
		}
		
		return removeCommands;		
	}
}
