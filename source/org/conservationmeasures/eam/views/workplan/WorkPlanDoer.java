/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.conservationmeasures.eam.views.workplan.WorkPlanTreeTableNode;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;

public abstract class WorkPlanDoer extends ViewDoer
{
	WorkPlanPanel getPanel()
	{
		WorkPlanPanel panel = ((WorkPlanView)getView()).getWorkPlanPanel();
		return panel;
	}
	
	public WorkPlanTreeTableNode getSelectedObject()
	{
		WorkPlanPanel panel = getPanel();
		if(panel == null)
			return null;
		return panel.getSelectedObject();
	}

	public Task getSelectedTask()
	{
		WorkPlanPanel panel = getPanel();
		if(panel == null)
			return null;
		return panel.getSelectedTask();
	}

}
