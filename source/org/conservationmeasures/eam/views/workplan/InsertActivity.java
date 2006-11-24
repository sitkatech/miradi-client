/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ActivityInsertionPoint;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class InsertActivity extends WorkPlanDoer
{
	public boolean isAvailable()
	{
		WorkPlanTreeTableNode selected = getSelectedObject();
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
		ModelNodeId interventionId = insertAt.getInterventionId();
		int childIndex = insertAt.getIndex();
		Factor intervention = getProject().getNodePool().find(interventionId);

		try
		{
			insertActivity(getProject(), intervention, childIndex);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static void insertActivity(Project project, Factor intervention, int childIndex) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();
	
			CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(intervention, 
					Strategy.TAG_ACTIVITY_IDS, createdId, childIndex);
			project.executeCommand(addChild);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		
	}
}
