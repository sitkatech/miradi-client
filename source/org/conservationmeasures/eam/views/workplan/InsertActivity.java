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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ActivityInsertionPoint;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class InsertActivity extends WorkPlanDoer
{
	public boolean isAvailable()
	{
		WorkPlanTreeTableNode selected = getSelectedObject();
		if(selected == null)
			return false;
		return (selected.canInsertActivityHere() || selected.canInsertTaskHere());
	}

	public void doIt() throws CommandFailedException
	{
		doInsertActivity();
	}

	private void doInsertActivity() throws CommandFailedException
	{
		ActivityInsertionPoint insertAt = getPanel().getActivityInsertionPoint();
		ORef proposedParentORef = insertAt.getProposedParentORef();
		int childIndex = insertAt.getIndex();
		EAMObject foundObject = getProject().getPool(proposedParentORef.getObjectType()).findObject(proposedParentORef.getObjectId());
		
		try
		{
			CommandSetObjectData commandSetObjectData = createSetObjectDataCommand(getProject(), foundObject, childIndex);
			insertActivity(getProject(), commandSetObjectData);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	
	public static CommandSetObjectData createSetObjectDataCommand(Project project, EAMObject object, int childIndex) throws Exception
	{
		CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
		project.executeCommand(create);
		BaseId createdId = create.getCreatedId();

		if (object.getType() == ObjectType.MODEL_NODE)
			return CommandSetObjectData.createInsertIdCommand(object, Strategy.TAG_ACTIVITY_IDS, createdId, childIndex);

		return CommandSetObjectData.createInsertIdCommand(object, Task.TAG_SUBTASK_IDS, createdId, childIndex);
	}

	public static void insertActivity(Project project, CommandSetObjectData addChildCommand) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			project.executeCommand(addChildCommand);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		
	}
}
