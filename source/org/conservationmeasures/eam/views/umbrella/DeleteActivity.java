/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public class DeleteActivity extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (getObjects() == null)
			return false;
		
		if ((getObjects().length != 1))
			return false;
		
		if (!(getObjects()[0].getType() == ObjectType.TASK))
			return false;
			
		//FIXME this must be true, fix for deleting tasks and subtasks
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Task activity = (Task)getObjects()[0];
		Strategy strategy = findStragetyForActivity(activity);

		try
		{
			deleteActivity(getProject(), strategy, activity);
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
	
	private Strategy findStragetyForActivity(Task activity)
	{
		Factor[] factors = getProject().getFactorPool().getInterventions();
		for (int i = 0; i < factors.length; i++)
			if (((Strategy)factors[i]).getActivityIds().contains(activity.getId()))
				return (Strategy)factors[i];

		return null;
	}


	WorkPlanView view;
}
