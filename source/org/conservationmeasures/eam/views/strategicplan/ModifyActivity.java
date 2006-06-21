/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyActivity extends ViewDoer
{
	public StratPlanObject getSelectedObject()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getSelectedObject();
	}
	
	public boolean isAvailable()
	{
		StratPlanObject selected = getSelectedObject();
		if(selected == null)
			return false;
		return (selected.getType() == ObjectType.TASK);
	}

	public void doIt() throws CommandFailedException
	{
		StratPlanActivity selected = (StratPlanActivity)getSelectedObject();
		getView().modifyTask(selected.getActivity());
	}
}
