/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.targetviability.wizard;

import org.conservationmeasures.eam.wizard.WizardPanel;
import org.conservationmeasures.eam.wizard.WizardStep;

public class TargetViability3Step extends WizardStep
{
	public TargetViability3Step(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "TargetViability3Step";

}
