/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.wizard.WizardPanel;
import org.conservationmeasures.eam.wizard.WizardStep;

public class MonitoringWizardFocusStep extends WizardStep
{
	public MonitoringWizardFocusStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	String HTML_FILENAME = "MonitoringWizardFocusStep";
}

