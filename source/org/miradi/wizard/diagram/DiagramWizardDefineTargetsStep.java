/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

public class DiagramWizardDefineTargetsStep extends DiagramWizardStep
{

	public DiagramWizardDefineTargetsStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpDiagramWizardDefineTargetsStep.class;
	}
	
	public String getSubHeading()
	{
		return EAM.text("1) Identify initial list of targets");
	}
}
