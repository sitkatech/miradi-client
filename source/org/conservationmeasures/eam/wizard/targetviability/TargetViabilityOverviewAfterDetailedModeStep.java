package org.conservationmeasures.eam.wizard.targetviability;

import org.conservationmeasures.eam.actions.views.ActionViewTargetViability;
import org.conservationmeasures.eam.wizard.TargetViabilityWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class TargetViabilityOverviewAfterDetailedModeStep extends TargetViabilityWizardStep
{
	public TargetViabilityOverviewAfterDetailedModeStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	public String getHtmlBaseName()
	{
		return getResourceFileName();
	}

	
	public Class getAssociatedActionClass()
	{
		return ActionViewTargetViability.class;
	}
	
	String HTML_FILENAME = "TargetViabilityOverviewStep";
}
