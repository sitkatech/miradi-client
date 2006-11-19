/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class NoProjectWizardWelcomeStep extends WizardStep
{
	public NoProjectWizardWelcomeStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public void linkClicked(String linkDescription)
	{
		NoProjectWizardPanel wizard = (NoProjectWizardPanel)getWizard();
		wizard.linkClicked(linkDescription);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	String HTML_FILENAME = "WelcomeStep.html";
}
