package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.wizard.WizardManager;


public class WizardPreviousDoer extends WizardNavigationDoer
{
	String getControlName()
	{
		return WizardManager.CONTROL_BACK;
	}
}
