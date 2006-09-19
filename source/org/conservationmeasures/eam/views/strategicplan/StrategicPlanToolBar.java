package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class StrategicPlanToolBar extends EAMToolBar
{
	public StrategicPlanToolBar(Actions actions)
	{
		super(actions, ActionViewStrategicPlan.class, getExtraButtons(actions));
	}
	
	static JComponent[][] getExtraButtons(Actions actions)
	{
		return new JComponent[0][0];
	}
	
}