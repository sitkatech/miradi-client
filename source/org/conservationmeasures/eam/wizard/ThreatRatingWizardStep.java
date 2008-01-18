/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;

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
