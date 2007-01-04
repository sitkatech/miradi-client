/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class MonitoringWizardOverviewStep extends WizardStep
{
	public MonitoringWizardOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:MonitoringPlan"))
		{
			EAM.okDialog("Definition: Monitoring Plan", new String[] {
					"A plan for collecting information that you and others " +
					"need to know about your project. A good play includes " +
					"the indicators that you will track over time as well as " +
					"the methods that you will use."});
		}
		
		if(linkDescription.equals("Definition:Indicator"))
		{
			EAM.okDialog("Definition: Indicator", new String[] {
					"A measurable entity related to a specific information " +
					"need (for example, the status of a target, change in a " +
					"threat, or progress towards an objective). A good " +
					"indicator meets the criteria of being: measurable, precise, " +
					"consistent, and sensitive."});
		}
	}
	
	String HTML_FILENAME = "MonitoringOverviewStep.html";
}
