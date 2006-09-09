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
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:ConceptualModel"))
		{
			EAM.okDialog("Definition: Conceptual Model", new String[] {"" +
					"Conceptual model - A diagram of a set of relationships " +
					"between certain factors that are believed to impact or " +
					"lead to a conservation target"});
		}
	}

	String HTML_FILENAME = "OverviewStep.html";
}
