/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.diagram;

import org.miradi.actions.views.ActionViewDiagram;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

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
