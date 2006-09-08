/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardOverviewStep extends WizardStep
{
	public DiagramWizardOverviewStep(WizardPanel panelToUse) 
	{
		super(panelToUse);
	}

	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	public boolean save() throws Exception
	{
		return true;
	}

	public void buttonPressed(String buttonName)
	{
		EAM.okDialog("Not implemented yet", new String[] {"Not implemented yet"});
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:ConceptualModel"))
		{
			EAM.okDialog("Definition: Conceptual Model", new String[] {"A conceptual model is..."});
		}
	}

	String HTML_FILENAME = "OverviewStep.html";
}
