/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.threatmatrix;

import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.wizard.ThreatRatingWizardStep;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.conservationmeasures.eam.wizard.WizardPanel;


public class ThreatMatrixOverviewStep extends ThreatRatingWizardStep
{
	public ThreatMatrixOverviewStep(WizardPanel wizardToUse) 
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewThreatMatrix.class;
	}
	
	public void buttonPressed(String buttonName)
	{
		if(isProjectInStressMode() && buttonName.equals(WizardManager.CONTROL_NEXT))
			buttonName = THREAT_OVERVIEW_STRESS_MODE;

		super.buttonPressed(buttonName);
	}

	public static final String THREAT_OVERVIEW_STRESS_MODE = "ThreatOverviewStressMode";
}


