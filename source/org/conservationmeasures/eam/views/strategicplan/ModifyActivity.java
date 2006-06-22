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
		StrategicPlanPanel panel = ((StrategicPlanView)getView()).getStrategicPlanPanel();
		if(panel == null)
			return null;
		return panel.getSelectedObject();
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
		try
		{
			StratPlanActivity selected = (StratPlanActivity)getSelectedObject();
			getView().modifyObject(selected.getActivity());
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
