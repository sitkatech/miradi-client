/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.diagram;

import org.miradi.actions.jump.ActionJumpSummaryWizardDefineProjectVision;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

public class DiagramWizardVisionStep extends DiagramWizardStep
{
	public DiagramWizardVisionStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpSummaryWizardDefineProjectVision.class;
	}
	
	@Override
	public String getSubHeading()
	{
		return EAM.text("Refine vision statement");
	}
}
