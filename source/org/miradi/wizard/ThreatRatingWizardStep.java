/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard;

import org.miradi.views.threatmatrix.ThreatMatrixView;

public abstract class ThreatRatingWizardStep extends SplitWizardStep
{
	public ThreatRatingWizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, ThreatMatrixView.getViewName());
	}

	protected boolean isProjectInStressMode()
	{
		boolean stressMode = getMainWindow().getProject().getMetadata().isStressBasedThreatRatingMode();
		return stressMode;
	}

}
