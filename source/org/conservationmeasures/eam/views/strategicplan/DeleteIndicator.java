/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteIndicator extends ViewDoer
{
	public ObjectiveManagementPanel getObjectivePanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getObjectivePanel();
	}
	
	
	public boolean isAvailable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		// TODO Auto-generated method stub

	}

}
