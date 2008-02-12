/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.targetviability;

import org.miradi.actions.views.ActionViewTargetViability;
import org.miradi.wizard.TargetViabilityWizardStep;
import org.miradi.wizard.WizardPanel;

public class TargetViabilityOverviewStep extends TargetViabilityWizardStep
{
	public TargetViabilityOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewTargetViability.class;
	}
	
}
