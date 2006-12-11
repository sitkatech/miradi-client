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
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class InsertActivity extends WorkPlanDoer
{
	public boolean isAvailable()
	{
		TreeTableNode selected = getSelectedObject();
		if(selected == null)
			return false;
		return canInsertHere(selected);
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
		EAMObject foundObject = getProject().findObject(proposedParentORef.getObjectType(), proposedParentORef.getObjectId()); 

		try
		{
			insertActivity(getProject(), foundObject, childIndex);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static void insertActivity(Project project, EAMObject object, int childIndex) throws CommandFailedException, ParseException, Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject create = new CommandCreateObject(ObjectType.TASK, new CreateTaskParameter(object.getRef()));
			project.executeCommand(create);
			BaseId createdId = create.getCreatedId();

			CommandSetObjectData addChildCommand;
			if (object.getType() == ObjectType.FACTOR)
				addChildCommand = CommandSetObjectData.createInsertIdCommand(object, Strategy.TAG_ACTIVITY_IDS, createdId, childIndex);
			else if (object.getType() == ObjectType.INDICATOR)
				addChildCommand = CommandSetObjectData.createInsertIdCommand(object, Indicator.TAG_TASK_IDS, createdId, childIndex);
			else
				addChildCommand = CommandSetObjectData.createInsertIdCommand(object, Task.TAG_SUBTASK_IDS, createdId, childIndex);
			
			project.executeCommand(addChildCommand);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private boolean canInsertHere(TreeTableNode selected)
	{
		int type = selected.getObjectReference().getObjectType();
		if (type == ObjectType.TASK)
			return true;
		if (type == ObjectType.INDICATOR)
			return true;
		
		return false;
	}


	
}
