/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ViewDoer;

public abstract class StratPlanDoer extends ViewDoer
{
	StrategicPlanPanel getPanel()
	{
		StrategicPlanPanel panel = ((StrategicPlanView)getView()).getStrategicPlanPanel();
		return panel;
	}
	
	public StratPlanObject getSelectedObject()
	{
		StrategicPlanPanel panel = getPanel();
		if(panel == null)
			return null;
		return panel.getSelectedObject();
	}

	public Task getSelectedTask()
	{
		StrategicPlanPanel panel = getPanel();
		if(panel == null)
			return null;
		return panel.getSelectedTask();
	}

}
