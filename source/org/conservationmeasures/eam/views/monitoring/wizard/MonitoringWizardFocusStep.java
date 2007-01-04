/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class MonitoringWizardFocusStep extends WizardStep
{
	public MonitoringWizardFocusStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:StrategyEffectiveness"))
		{
			EAM.okDialog("Definition: Strategy Effectiveness", new String[] {
					"Information used to answer the question: Are the conservation actions " +
					"we are taking achieving their desired results?"});
		}
		
		if(linkDescription.equals("Definition:StatusAssessments"))
		{
			EAM.okDialog("Definition: Status Assessments", new String[] {
					"Information used to answer the questions how are key targets, threats, " +
					"and other factors changing? Answers to these questions, even when no " +
					"actions are occurring, are important to determine if future actions are needed."});
		}
	}
	
	String HTML_FILENAME = "MonitoringFocusStep.html";
}

