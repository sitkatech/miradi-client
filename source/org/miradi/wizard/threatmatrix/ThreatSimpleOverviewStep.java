/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.threatmatrix;

import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.main.EAM;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.ThreatRatingWizardStep;
import org.miradi.wizard.WizardPanel;

public class ThreatSimpleOverviewStep extends ThreatRatingWizardStep
{
	public ThreatSimpleOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1C;
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewThreatMatrix.class;
	}
	
	public String getSubHeading()
	{
		return EAM.text("Simple Threat Rating");
	}
}
