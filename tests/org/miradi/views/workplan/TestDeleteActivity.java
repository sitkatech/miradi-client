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
package org.miradi.views.workplan;

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.umbrella.DeleteActivity;
import org.miradi.views.umbrella.Undo;

public class TestDeleteActivity extends EAMTestCase
{
	public TestDeleteActivity(String name)
	{
		super(name);
	}

	public void testDeleteActivity() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		try
		{
			ORef strategyRef = project.createObject(Strategy.getObjectType());
			Strategy strategy = (Strategy)project.findObject(strategyRef);

			ORef parentHasChildRef = project.createObject(Task.getObjectType());
			Task parentHasChild = (Task)project.findObject(parentHasChildRef);
			
			ORef parentHasNoChildRef  = project.createObject(Task.getObjectType());
			Task parentNoChild = (Task)project.findObject(parentHasNoChildRef);
			
			ORef leafChildRef = project.createObject(Task.getObjectType());
			Task leafChild = (Task)project.findObject(leafChildRef);
			
			CommandSetObjectData addResource1 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentNoChild.getId());
			project.executeCommand(addResource1);
			
			CommandSetObjectData addResource2 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentHasChild.getId());
			project.executeCommand(addResource2);
			
			CommandSetObjectData addResource3 = CommandSetObjectData.createAppendIdCommand(parentHasChild, Task.TAG_SUBTASK_IDS, leafChild.getId());
			project.executeCommand(addResource3);
			
			assertEquals("Parent doesn't have child?", 1, parentHasChild.getSubtaskCount());
			transactionDeleteTask(project, parentHasChildRef, leafChild);
			assertEquals("Didn't delete subtasks?", 0, parentHasChild.getSubtaskCount());
			Undo.undo(project);
			assertEquals("Didn't restore subtasks?", 1, parentHasChild.getSubtaskCount());
			
			transactionDeleteTask(project, strategy.getRef(), parentNoChild);
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			Undo.undo(project);
			assertEquals("Didn't restore activity?", 2, strategy.getActivityIds().size());
			
			transactionDeleteTask(project, strategy.getRef(), parentHasChild);
			parentHasChild = null;
		
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			Undo.undo(project);
			assertEquals("Didn't delete activity?", 2, strategy.getActivityIds().size());
			
			parentHasChild = (Task)project.findObject(parentHasChildRef);
			assertEquals("Didn't restore child?", 1, parentHasChild.getSubtaskCount());
		}
		finally
		{
			project.close();
		}
	}

	private void transactionDeleteTask(Project project, ORef parentRef, Task leafChild) throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList parentChildHierarchy = new ORefList();
			parentChildHierarchy.add(parentRef);
			DeleteActivity.deleteTaskTree(project, parentChildHierarchy, leafChild);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}

	}
	
	public void testBuildRemoveFromObjectiveRelevancyListCommands() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		ORef objectiveRef = project.createObject(Objective.getObjectType());
		Cause cause = project.createCause();
		IdList objectiveIdList = new ORefList(objectiveRef).convertToIdList(Objective.getObjectType());
		cause.setData(Cause.TAG_OBJECTIVE_IDS, objectiveIdList.toString());
		
		Strategy strategy = project.createAndPopulateStrategy();
		RelevancyOverrideSet relevancyOverrideSet = new RelevancyOverrideSet();
		RelevancyOverride relevancyOverride = new RelevancyOverride(strategy.getRef(), true);
		relevancyOverrideSet.add(relevancyOverride);
		
		Objective objective = Objective.find(project, objectiveRef);
		objective.setData(Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrideSet.toString());
		
		assertEquals("relevancy override was not set?", 1, objective.getRelevantStrategyAndActivityRefs().size());
																	   
		Vector<Command> commandsToUpdateRelevancyList = DeleteActivity.buildRemoveFromObjectiveRelevancyListCommands(project, Objective.getObjectType(), Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, strategy.getRef());
		project.executeCommandsWithoutTransaction(commandsToUpdateRelevancyList);
		
		assertEquals("relevancy override was not updated after delete?", 0, objective.getRelevantStrategyAndActivityRefs().size());
	}
}
