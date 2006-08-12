/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Task;
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
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			int type = activity.getType();
			BaseId id = activity.getId();
			
			ConceptualModelIntervention intervention = getStrategicPlanPanel().getParentIntervention(activity);
			CommandSetObjectData removeChild = CommandSetObjectData.createRemoveIdCommand(intervention, 
					ConceptualModelIntervention.TAG_ACTIVITY_IDS, id);
			getProject().executeCommand(removeChild);

			Task rootTask = getProject().getRootTask();
			CommandSetObjectData removeSubtask = CommandSetObjectData.createRemoveIdCommand(rootTask, Task.TAG_SUBTASK_IDS, id);
			getProject().executeCommand(removeSubtask);
	
			getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, ""));
			getProject().executeCommand(new CommandDeleteObject(type, id));
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	

	StrategicPlanView view;
}
