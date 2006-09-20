package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;
import org.conservationmeasures.eam.main.EAMToolBar;

public class WorkPlanToolBar extends EAMToolBar
{
	public WorkPlanToolBar(Actions actions)
	{
		super(actions, ActionViewWorkPlan.class);
	}

}
