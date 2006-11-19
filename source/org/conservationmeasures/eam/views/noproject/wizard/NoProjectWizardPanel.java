/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject.wizard;

import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class NoProjectWizardPanel extends WizardPanel
{
	public NoProjectWizardPanel(NoProjectView viewToUse) throws Exception
	{
		view = viewToUse;
		
		int WELCOME = addStep(new NoProjectWizardWelcomeStep(this));

		setStep(WELCOME);
	}
	
	public void linkClicked(String linkDescription)
	{
		view.linkClicked(linkDescription);
	}

	NoProjectView view;
}
