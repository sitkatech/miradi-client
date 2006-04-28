package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class StrategicPlanToolBar extends EAMToolBar
{
	public StrategicPlanToolBar(Actions actions)
	{
		super(actions, ActionViewStrategicPlan.class);
	}
}