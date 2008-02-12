/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

public class DiagramWizardReviewModelAndAdjustStep extends DiagramWizardStep
{

	public DiagramWizardReviewModelAndAdjustStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1D;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpDiagramWizardReviewModelAndAdjustStep.class;
	}
	
}
