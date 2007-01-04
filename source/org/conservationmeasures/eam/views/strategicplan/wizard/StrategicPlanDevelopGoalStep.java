/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.Definition;
import org.conservationmeasures.eam.views.umbrella.DefinitionCommonTerms;
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
		Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
		EAM.okDialog("Definition:" + def.term, new String[] {def.definition});
	}
	
	private static final String HTML_FILE_NAME = "DevelopGoals.html";
}

