/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteActivity extends ViewDoer
{
	public DeleteActivity(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return view.getStrategicPlanPanel();
	}
	
	public boolean isAvailable()
	{
		if(getStrategicPlanPanel() == null)
			return false;
		
		return getStrategicPlanPanel().getSelectedTask() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Task activity = getStrategicPlanPanel().getSelectedTask();
		ConceptualModelIntervention intervention = getStrategicPlanPanel().getParentIntervention(activity);
		try
		{
			deleteActivity(getProject(), intervention, activity);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public static void deleteActivity(Project project, ConceptualModelIntervention intervention, Task activity) throws ParseException, CommandFailedException
	{
		int type = activity.getType();
		BaseId id = activity.getId();
		
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			CommandSetObjectData removeChild = CommandSetObjectData.createRemoveIdCommand(intervention, 
					ConceptualModelIntervention.TAG_ACTIVITY_IDS, id);
			project.executeCommand(removeChild);
	
			project.executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, ""));
			project.executeCommand(new CommandSetObjectData(type, id, Task.TAG_RESOURCE_IDS, ""));
			project.executeCommand(new CommandDeleteObject(type, id));
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	StrategicPlanView view;
}
