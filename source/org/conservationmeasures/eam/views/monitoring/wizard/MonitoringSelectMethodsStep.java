/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class MonitoringSelectMethodsStep extends WizardStep
{
	public MonitoringSelectMethodsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	public void linkClicked(String linkDescription)
	{

		if(linkDescription.equals("Definition:Method"))
		{
			EAM.okDialog("Definition: Method",new String[] { 
					"A specific technique used to collect data to measure an indicator.  " +
					"Methods vary in their accuracy and reliability, cost-effectiveness, " +
					"feasibility, and appropriateness." });
		}
	}
	String HTML_FILENAME = "MonitoringSelectMethodsStep.html";
}