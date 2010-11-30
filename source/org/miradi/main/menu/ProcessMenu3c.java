/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main.menu;

import java.awt.event.KeyEvent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpImplementStrategicAndMonitoringPlans;
import org.miradi.actions.jump.ActionJumpImplementWorkPlan;

public class ProcessMenu3c extends MiradiMenu
{
	public ProcessMenu3c(Actions actions)
	{
		super(ProcessSteps.PROCESS_STEP_3C, actions);
		setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(ActionJumpImplementStrategicAndMonitoringPlans.class, KeyEvent.VK_S);
		addMenuItem(ActionJumpImplementWorkPlan.class, KeyEvent.VK_W);
	}
}
