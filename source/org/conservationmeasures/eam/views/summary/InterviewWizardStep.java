/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public abstract class InterviewWizardStep extends WizardStep
{
	public InterviewWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public boolean save() throws Exception
	{
		return true;
	}

}
