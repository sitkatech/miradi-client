/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteActivity extends ViewDoer
{
	public DeleteActivity(WorkPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public WorkPlanPanel getWorkPlanPanel()
	{
		return view.getWorkPlanPanel();
	}
	
	public boolean isAvailable()
	{
		if(getWorkPlanPanel() == null)
			return false;
		
		return getWorkPlanPanel().getSelectedTask() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Task activity = getWorkPlanPanel().getSelectedTask();
		Strategy intervention = getWorkPlanPanel().getParentIntervention(activity);
		try
		{
			deleteActivity(getProject(), intervention, activity);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public static void deleteActivity(Project project, Strategy intervention, Task activity) throws ParseException, CommandFailedException
	{
		int type = activity.getType();
		BaseId id = activity.getId();
		
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandSetObjectData removeChild = CommandSetObjectData.createRemoveIdCommand(intervention, 
					Strategy.TAG_ACTIVITY_IDS, id);
			project.executeCommand(removeChild);
	
			project.executeCommands(activity.createCommandsToClear());
			project.executeCommand(new CommandDeleteObject(type, id));
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	WorkPlanView view;
}
