/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class SummaryWizardDefineTeamMembers extends WizardStep
{
	public SummaryWizardDefineTeamMembers(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("InitialProjectTeam"))
		{
			EAM.okDialog("Definition:Initial Project Team", new String[] {
					" TO BE SPECIFIED "});
		}
	}
	
	String HTML_FILENAME = "SummaryDefineTeamMembersStep.html";
}

