/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.views.workplan;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
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
}
