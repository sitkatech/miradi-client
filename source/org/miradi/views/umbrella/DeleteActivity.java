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

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
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

	public static void deleteTaskWithUserConfirmation(Project project, ORefList selectionHierachy, Task selectedTask) throws CommandFailedException
	{
		Vector dialogText = new Vector();
		boolean containsMoreThanOneParent = selectionHierachy.getOverlappingRefs(selectedTask.findObjectsThatReferToUs()).size() > 1;
		if (containsMoreThanOneParent)
			dialogText.add(EAM.text("This item is shared, so will be deleted from multiple places."));
		
		dialogText.add(EAM.text("All subtasks within this item will be removed as well."));
		String[] buttons = {EAM.text("Button|Delete"), EAM.text("Button|Retain"), };
		if(!EAM.confirmDialog(EAM.text("Title|Delete"), (String[]) dialogText.toArray(new String[0]), buttons))
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
		Vector commandToDeleteTasks = createDeleteCommands(project, selectionHierachy, selectedTask); 
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

	private static Vector createDeleteCommands(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		
		//FIXME need to consider parent hierachy when creating commands.  first refactor dup code.  
		Vector commandsToDeleteTasks = new Vector();
		commandsToDeleteTasks.addAll(buildRemoveFromObjectiveRelevancyListCommands(project, task));
		commandsToDeleteTasks.addAll(buildDeleteDiagramFactors(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForActivityIds(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForMethodIds(project, selectionHierachy, task));
		commandsToDeleteTasks.addAll(buildRemoveCommandsForTaskIds(project, task));
		
		return commandsToDeleteTasks;
	}
	
	private static Vector buildRemoveCommandsForActivityIds(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		if (! task.isActivity())
			return new Vector();
		
		return buildRemoveCommands(project, Strategy.getObjectType(), selectionHierachy, Strategy.TAG_ACTIVITY_IDS, task);
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
	
	private static Vector<Command> buildDeleteDiagramFactors(Project project, ORefList selectionHierachy, Task task) throws Exception
	{
		Vector<Command> commands = new Vector();
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
					commands.addAll(activityDiagramFactor.createCommandsToClearAsList());
					commands.add(new CommandDeleteObject(activityDiagramFactor));
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
	
	private static Vector buildRemoveCommands(Project project, int parentType, ORefList selectionHierachy, String tag, Task task) throws Exception
	{
		Vector removeCommands = new Vector();
		ORefList referrerRefs = task.findObjectsThatReferToUs(parentType);
		for (int i = 0; i < referrerRefs.size(); ++i)
		{
			BaseObject referrer = project.findObject(referrerRefs.get(i));
			if (selectionHierachy.contains(referrer.getRef()))
				removeCommands.add(CommandSetObjectData.createRemoveIdCommand(referrer, tag, task.getId()));
		}
		
		return removeCommands;		
	}

	public static Vector<Command> buildRemoveFromObjectiveRelevancyListCommands(Project project, BaseObject objectToRemove) throws Exception
	{
		Vector<Command> removeFromRelevancyListCommands = new Vector();
		ORefList objectiveRefs = project.getObjectivePool().getORefList();
		for (int index = 0; index < objectiveRefs.size(); ++index)
		{
			Objective objective = Objective.find(project, objectiveRefs.get(index));
			RelevancyOverrideSet relevancyOverrideSet = objective.getStrategyActivityRelevancyOverrideSet();
			relevancyOverrideSet.remove(objectToRemove.getRef());
			CommandSetObjectData removeFromRelevancyListCommand = new CommandSetObjectData(objective.getRef(), Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrideSet.toString());
			removeFromRelevancyListCommands.add(removeFromRelevancyListCommand);
		}
		
		return removeFromRelevancyListCommands;
	}
}
