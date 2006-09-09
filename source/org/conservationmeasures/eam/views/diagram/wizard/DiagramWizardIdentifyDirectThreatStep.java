/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardIdentifyDirectThreatStep extends WizardStep
{

	public DiagramWizardIdentifyDirectThreatStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:DirectThreat"))
		{
			EAM.okDialog("Definition: Direct Threat", new String[] {"" +
					"Direct threat – Proximate agents or factors that directly" +
					"degrade conservation targets."});
		}
	}

	public String getResourceFileName()
	{
		return "IdentifyDirectThreatStep.html";
	}

}
