/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.schedule;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewSchedule;
import org.miradi.main.EAMToolBar;

public class ScheduleToolBar extends EAMToolBar
{
	public ScheduleToolBar(Actions actions)
	{
		super(actions, ActionViewSchedule.class);
	}
}
