/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.wizard.planning;

import org.miradi.actions.jump.ActionJumpPlanningWizardImplementPlans;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.views.workplan.WorkPlanView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

public class ImplementPlans extends SplitWizardStep
{
	public ImplementPlans(WizardPanel wizardToUse)
	{
		super(wizardToUse, WorkPlanView.getViewName());
	}

	@Override
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3C;
	}

	@Override
	public Class getAssociatedActionClass()
	{
		return ActionJumpPlanningWizardImplementPlans.class;
	}


}
