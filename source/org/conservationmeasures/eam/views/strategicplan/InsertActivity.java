/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertActivity extends ProjectDoer
{
	public InsertActivity(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public boolean isAvailable()
	{
		StratPlanObject selected = view.getSelectedObject();
		if(selected == null)
			return false;
		return selected.canInsertActivityHere();
	}

	public void doIt() throws CommandFailedException
	{
		StratPlanObject selected = view.getSelectedObject();
		EAM.logWarning(selected.toString());
	}

	StrategicPlanView view;
}
