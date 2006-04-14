package org.conservationmeasures.eam.views.calendar;

import org.conservationmeasures.eam.actions.ActionViewCalendar;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class CalendarToolBar extends EAMToolBar
{
	public CalendarToolBar(Actions actions)
	{
		super(actions, ActionViewCalendar.class);
	}

}
