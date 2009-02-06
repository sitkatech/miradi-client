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
package org.miradi.wizard.planning;

import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WorkPlanWizardStep;

public class WorkPlanDevelopMethodsAndTasksStep extends WorkPlanWizardStep
{
	public WorkPlanDevelopMethodsAndTasksStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_3A;
	}

	@Override
	public String getWizardScreenTitle()
	{
		return EAM.text("Detail activities/methods, tasks, and responsibilities");
	}

	public String getSubHeading()
	{
		return EAM.text("2) Methods");
	}
}
