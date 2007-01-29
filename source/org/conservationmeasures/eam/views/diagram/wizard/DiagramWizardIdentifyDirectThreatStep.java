/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.views.umbrella.IWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardIdentifyDirectThreatStep extends WizardStep
{

	public DiagramWizardIdentifyDirectThreatStep(IWizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getResourceFileName()
	{
		return "IdentifyDirectThreatStep.html";
	}

}
