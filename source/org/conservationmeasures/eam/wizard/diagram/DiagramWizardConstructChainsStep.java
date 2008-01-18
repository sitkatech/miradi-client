/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.diagram;

import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardConstructChainsStep;
import org.conservationmeasures.eam.main.menu.ProcessSteps;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class DiagramWizardConstructChainsStep extends DiagramWizardStep
{

	public DiagramWizardConstructChainsStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1D;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpDiagramWizardConstructChainsStep.class;
	}
	
}
