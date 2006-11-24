/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.umbrella.Redo;
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
			CreateFactorParameter parameter = new CreateFactorParameter(new FactorTypeStrategy());
			BaseId rawInterventionId = project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, parameter);
			FactorId interventionId = new FactorId(rawInterventionId.asInt());
			Strategy intervention = (Strategy)project.findNode(interventionId);
			BaseId resourceId = project.createObject(ObjectType.PROJECT_RESOURCE);
	//		ProjectResource resource = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, resourceId);
			
			InsertActivity.insertActivity(project, intervention, 0);
			BaseId activityId = intervention.getActivityIds().get(0);
			Task activity = (Task)project.findObject(ObjectType.TASK, activityId);
			CommandSetObjectData addResource = CommandSetObjectData.createAppendIdCommand(activity, Task.TAG_RESOURCE_IDS, resourceId);
			project.executeCommand(addResource);
			
			DeleteActivity.deleteActivity(project, intervention, activity);
			Undo.undo(project);
			activity = (Task)project.findObject(ObjectType.TASK, activityId);
			assertEquals("Didn't restore resource?", 1, activity.getResourceCount());
			Undo.undo(project);
			Redo.redo(project);
			Redo.redo(project);
		}
		finally
		{
			project.close();
		}
	}
}
