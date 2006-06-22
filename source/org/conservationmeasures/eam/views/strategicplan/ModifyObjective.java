/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyObjective extends ViewDoer
{
	public ObjectiveManagementPanel getObjectivePanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getObjectivePanel();
	}
	
	public boolean isAvailable()
	{
		if(getObjectivePanel() == null)
			return false;
		
		return getObjectivePanel().getSelectedObjective() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			Objective objective = getObjectivePanel().getSelectedObjective();
			getView().modifyObject(objective);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
