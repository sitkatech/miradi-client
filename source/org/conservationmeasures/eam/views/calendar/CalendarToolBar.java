package org.conservationmeasures.eam.views.calendar;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewCalendar;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class CalendarToolBar extends JToolBar
{
	public CalendarToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewCalendar.class));
	}

}
