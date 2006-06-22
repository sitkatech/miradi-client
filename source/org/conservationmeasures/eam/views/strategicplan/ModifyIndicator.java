/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyIndicator extends ViewDoer
{
	public IndicatorManagementPanel getIndicatorPanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getIndicatorManagementPanel();
	}
	
	public boolean isAvailable()
	{
		if(getIndicatorPanel() == null)
			return false;
		
		return getIndicatorPanel().getSelectedIndicator() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			Indicator indicator = getIndicatorPanel().getSelectedIndicator();
			getView().modifyObject(indicator);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
