/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class StrategicPlanDevelopGoalStep extends WizardStep
{
	public StrategicPlanDevelopGoalStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILE_NAME;
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Goal"))
		{
			EAM.okDialog("Definition:Goals", new String[] {
				"Goal -- A formal statement detailing a desired impact of a project.  " +
				"In conservation projects, it is the desired future status of a target." });
		}
	}
	
	private static final String HTML_FILE_NAME = "DevelopGoals.html";
}
