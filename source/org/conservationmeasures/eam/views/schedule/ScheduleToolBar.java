package org.conservationmeasures.eam.views.schedule;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewSchedule;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class ScheduleToolBar extends JToolBar
{
	public ScheduleToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewSchedule.class));
	}

}
