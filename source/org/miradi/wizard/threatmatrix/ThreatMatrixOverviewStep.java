/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.threatmatrix;

import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.wizard.ThreatRatingWizardStep;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;


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


