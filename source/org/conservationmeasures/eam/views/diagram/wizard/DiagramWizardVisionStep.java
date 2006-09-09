/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardVisionStep extends WizardStep
{
	public DiagramWizardVisionStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:Vision"))
		{
			EAM.okDialog("Definition: Vision", new String[] {"Vision Statement - A general summary of the desired state or ultimate condition of the project area that a project is working to achieve"});
		}
	}

	public String getResourceFileName()
	{
		return "VisionStep.html";
	}
	
}
