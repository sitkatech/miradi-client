/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class SummaryWizardDefineProjectVision extends WizardStep
{
	public SummaryWizardDefineProjectVision(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:VisionStatement"))
		{
			EAM.okDialog("Definition: Vision Statement", new String[] {
					"A general summary of the desired state " +
					"or ultimate condition of the project area that a project is working to achieve"});
		}
	}
	
	String HTML_FILENAME = "SummaryDefineProjectVisionStep.html";
}
