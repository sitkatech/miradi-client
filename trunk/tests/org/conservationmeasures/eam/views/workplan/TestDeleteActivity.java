/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;
import org.conservationmeasures.eam.views.umbrella.Undo;

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
			BaseId rawStrategyId = project.createObject(ObjectType.STRATEGY);
			FactorId strategyId = new FactorId(rawStrategyId.asInt());
			Strategy strategy = (Strategy)project.findNode(strategyId);

			BaseId parentHasChildId = project.createObject(ObjectType.TASK, BaseId.INVALID);
			Task parentHasChild = (Task)project.findObject(ObjectType.TASK, parentHasChildId);
			
			BaseId parentHasNoChildId  = project.createObject(ObjectType.TASK, BaseId.INVALID);
			Task parentNoChild = (Task)project.findObject(ObjectType.TASK, parentHasNoChildId);
			
			BaseId leafChildId = project.createObject(ObjectType.TASK, BaseId.INVALID);
			Task leafChild = (Task)project.findObject(ObjectType.TASK, leafChildId);
			
			CommandSetObjectData addResource1 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentNoChild.getId());
			project.executeCommand(addResource1);
			
			CommandSetObjectData addResource2 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentHasChild.getId());
			project.executeCommand(addResource2);
			
			CommandSetObjectData addResource3 = CommandSetObjectData.createAppendIdCommand(parentHasChild, Task.TAG_SUBTASK_IDS, leafChild.getId());
			project.executeCommand(addResource3);
			
			assertEquals("Parent doesn't have child?", 1, parentHasChild.getSubtaskCount());
			transactionDeleteTask(project, leafChild);
			assertEquals("Didn't delete subtasks?", 0, parentHasChild.getSubtaskCount());
			Undo.undo(project);
			assertEquals("Didn't restore subtasks?", 1, parentHasChild.getSubtaskCount());
			
			transactionDeleteTask(project, parentNoChild);
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			Undo.undo(project);
			assertEquals("Didn't restore activity?", 2, strategy.getActivityIds().size());
			
			transactionDeleteTask(project, parentHasChild);
			parentHasChild = null;
		
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			Undo.undo(project);
			assertEquals("Didn't delete activity?", 2, strategy.getActivityIds().size());
			
			parentHasChild = (Task)project.findObject(ObjectType.TASK, parentHasChildId);
			assertEquals("Didn't restore child?", 1, parentHasChild.getSubtaskCount());
		}
		finally
		{
			project.close();
		}
	}

	private void transactionDeleteTask(Project project, Task leafChild) throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			DeleteActivity.deleteTaskTree(project, leafChild);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}

	}
}
