/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.planning;

import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.WorkPlanWizardStep;

public class WorkPlanCreateResourcesStep extends WorkPlanWizardStep
{
	public WorkPlanCreateResourcesStep(WizardPanel wizardToUse)
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
		return EAM.text("3) Edit resources");
	}
}
