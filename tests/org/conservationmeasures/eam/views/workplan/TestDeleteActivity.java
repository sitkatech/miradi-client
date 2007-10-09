/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
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
			
			transactionDeleteTask(project, parentNoChild.getRef(), parentNoChild);
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			Undo.undo(project);
			assertEquals("Didn't restore activity?", 2, strategy.getActivityIds().size());
			
			transactionDeleteTask(project, parentHasChildRef, parentHasChild);
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
