/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ResourceListAdd extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if(getObjects().length == 0)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		BaseId selectedResourceId = getSelectedId();
		try
		{
			Task selectedActivity = (Task)((WorkPlanView)getView()).getSelectedObject();
			Command cmd = CommandSetObjectData.createAppendIdCommand(selectedActivity, selectedActivity.TAG_RESOURCE_IDS, selectedResourceId);
			getProject().executeCommand(cmd);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Text|Unknown error prevented adding this person to the team"));
		}
	}
}
