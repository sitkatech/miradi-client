/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.diagram;

import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineProjectVision;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

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
}
