/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class MonitoringWizardEditIndicatorsStep  extends WizardStep
{
	public MonitoringWizardEditIndicatorsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Indicator"))
		{
			EAM.okDialog("Definition: Indicator", new String[] {
					"A measurable entity related to a specific information need " +
					"(for example, the status of a target, change in a threat, " +
					"or progress towards an objective).  " +
					"A good indicator meets the criteria of being: measurable, " +
					"precise, consistent, and sensitive."});
		}
	}
	
	String HTML_FILENAME = "MonitoringEditIndicatorsStep.html";
}

