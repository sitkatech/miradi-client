/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.schedule;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;
import org.conservationmeasures.eam.main.EAMToolBar;

public class ScheduleToolBar extends EAMToolBar
{
	public ScheduleToolBar(Actions actions)
	{
		super(actions, ActionViewWorkPlan.class);
	}
}
