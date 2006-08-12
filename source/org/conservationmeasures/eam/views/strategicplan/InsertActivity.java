/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ActivityInsertionPoint;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ViewDoer;

public class InsertActivity extends ViewDoer
{
	public StratPlanObject getSelectedObject()
	{
		StrategicPlanPanel panel = getPanel();
		if(panel == null)
			return null;
		return panel.getSelectedObject();
	}

	private StrategicPlanPanel getPanel()
	{
		StrategicPlanPanel panel = ((StrategicPlanView)getView()).getStrategicPlanPanel();
		return panel;
	}
	
	public boolean isAvailable()
	{
		StratPlanObject selected = getSelectedObject();
		if(selected == null)
			return false;
		return selected.canInsertActivityHere();
	}

	public void doIt() throws CommandFailedException
	{
		doInsertActivity();
	}

	private void doInsertActivity() throws CommandFailedException
	{
		ActivityInsertionPoint insertAt = getPanel().getActivityInsertionPoint();
		BaseId interventionId = insertAt.getInterventionId();
		int childIndex = insertAt.getIndex();
		ConceptualModelNode intervention = getProject().getNodePool().find(interventionId);

		try
		{
			getProject().executeCommand(new CommandBeginTransaction());
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			getProject().executeCommand(create);
			BaseId createdId = create.getCreatedId();

			Task rootTask = getProject().getRootTask();
			CommandSetObjectData addSubtask = CommandSetObjectData.createInsertIdCommand(rootTask, Task.TAG_SUBTASK_IDS, createdId, rootTask.getSubtaskCount());
			getProject().executeCommand(addSubtask);

			CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(intervention, 
					ConceptualModelIntervention.TAG_ACTIVITY_IDS, createdId, childIndex);
			getProject().executeCommand(addChild);
			
			getProject().executeCommand(new CommandEndTransaction());
			
			Task activity = getProject().getTaskPool().find(createdId);
			getView().modifyObject(activity);
			getView().selectObject(activity);

		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
}
