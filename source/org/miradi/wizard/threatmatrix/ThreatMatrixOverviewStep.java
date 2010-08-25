/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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


