/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class SummaryWizardDefineProjecScope extends WizardStep
{
	public SummaryWizardDefineProjecScope(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Scope"))
		{
			EAM.okDialog("Definition: Scope", new String[] {
					"The broad geographic or thematic focus of a project"});
		}
	}
	
	String HTML_FILENAME = "SummaryDefineProjectScopeStep.html";
}