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
package org.miradi.views.workplan;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.views.umbrella.DeleteActivityDoer;
import org.miradi.views.umbrella.UndoDoer;

public class TestDeleteActivity extends TestCaseWithProject
{
	public TestDeleteActivity(String name)
	{
		super(name);
	}

	public void testDeleteActivity() throws Exception
	{
			ORef strategyRef = getProject().createObject(Strategy.getObjectType());
			Strategy strategy = (Strategy)getProject().findObject(strategyRef);

			ORef parentHasChildRef = getProject().createObject(Task.getObjectType());
			Task parentHasChild = (Task)getProject().findObject(parentHasChildRef);
			
			ORef parentHasNoChildRef  = getProject().createObject(Task.getObjectType());
			Task parentNoChild = (Task)getProject().findObject(parentHasNoChildRef);
			
			ORef leafChildRef = getProject().createObject(Task.getObjectType());
			Task leafChild = (Task)getProject().findObject(leafChildRef);
			
			CommandSetObjectData addResource1 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentNoChild.getId());
			getProject().executeCommand(addResource1);
			
			CommandSetObjectData addResource2 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_ACTIVITY_IDS, parentHasChild.getId());
			getProject().executeCommand(addResource2);
			
			CommandSetObjectData addResource3 = CommandSetObjectData.createAppendIdCommand(parentHasChild, Task.TAG_SUBTASK_IDS, leafChild.getId());
			getProject().executeCommand(addResource3);
			
			assertEquals("Parent doesn't have child?", 1, parentHasChild.getSubtaskCount());
			transactionDeleteTask(getProject(), parentHasChildRef, leafChild);
			assertEquals("Didn't delete subtasks?", 0, parentHasChild.getSubtaskCount());
			UndoDoer.undo(getProject());
			assertEquals("Didn't restore subtasks?", 1, parentHasChild.getSubtaskCount());
			
			transactionDeleteTask(getProject(), strategy.getRef(), parentNoChild);
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			UndoDoer.undo(getProject());
			assertEquals("Didn't restore activity?", 2, strategy.getActivityIds().size());
			
			transactionDeleteTask(getProject(), strategy.getRef(), parentHasChild);
			parentHasChild = null;
		
			assertEquals("Didn't delete activity?", 1, strategy.getActivityIds().size());
			UndoDoer.undo(getProject());
			assertEquals("Didn't delete activity?", 2, strategy.getActivityIds().size());
			
			parentHasChild = (Task)getProject().findObject(parentHasChildRef);
			assertEquals("Didn't restore child?", 1, parentHasChild.getSubtaskCount());
	}

	private void transactionDeleteTask(Project project, ORef parentRef, Task leafChild) throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList parentChildHierarchy = new ORefList();
			parentChildHierarchy.add(parentRef);
			DeleteActivityDoer.deleteTaskTree(project, parentChildHierarchy, leafChild);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}

	}
}
