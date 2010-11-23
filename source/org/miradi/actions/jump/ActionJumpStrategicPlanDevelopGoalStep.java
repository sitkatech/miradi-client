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
package org.miradi.actions.jump;

import java.awt.event.KeyEvent;

import org.miradi.actions.AbstractMenuAction;
import org.miradi.main.MainWindow;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;

public class ActionJumpStrategicPlanDevelopGoalStep extends AbstractMenuAction
{
	public ActionJumpStrategicPlanDevelopGoalStep(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, OpenStandardsPlanActionsAndMonitoringQuestion.getDevelopGoalsForEachTargetLabel());
	}
	
	@Override
	public int getMnemonic()
	{
		return KeyEvent.VK_G;
	}

	@Override
	public String getCode()
	{
		return OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_GOALS_FOR_EACH_TARGET_CODE;
	}
}
