/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardStep;

public abstract class ThreatRatingWizardStep extends WizardStep
{
	public ThreatRatingWizardStep(ThreatRatingWizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	abstract void refresh() throws Exception;

	ThreatRatingWizardPanel getThreatRatingWizard()
	{
		return (ThreatRatingWizardPanel)getWizard();
	}

}
