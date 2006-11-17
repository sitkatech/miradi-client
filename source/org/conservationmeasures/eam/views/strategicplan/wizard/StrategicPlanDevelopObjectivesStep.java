/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class StrategicPlanDevelopObjectivesStep extends WizardStep
{
	public StrategicPlanDevelopObjectivesStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILE_NAME;
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Objective"))
		{
			EAM.okDialog("Definition:Objective", new String[] {
				"Objective – A formal statement detailing a desired " +
				"outcome of a project, such as reducing a critical threat." });
		}
	}

	
	private static final String HTML_FILE_NAME = "DevelopObjectives.html";
}
