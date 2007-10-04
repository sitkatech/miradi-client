/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.diagram;

import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.wizard.DiagramWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class DiagramOverviewStep extends DiagramWizardStep
{
	public DiagramOverviewStep(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewDiagram.class;
	}
}
