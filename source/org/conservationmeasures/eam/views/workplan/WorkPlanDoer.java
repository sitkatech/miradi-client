/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.ViewDoer;

public abstract class WorkPlanDoer extends ViewDoer
{
	WorkPlanPanel getPanel()
	{
		return null;
	}
	
	public TreeTableNode getSelectedObject()
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
