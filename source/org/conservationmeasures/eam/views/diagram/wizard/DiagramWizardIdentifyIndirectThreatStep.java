/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardIdentifyIndirectThreatStep extends WizardStep
{

	public DiagramWizardIdentifyIndirectThreatStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:IndirectThreat"))
		{
			EAM.okDialog("Definition: Contributing Factor", new String[] {"" +
					"Contributing factors (Indirect threats and Opportunities)" +
					" – Human-induced actions and event that underlie or lead" +
					" to the direct threats"});
		}
	}

	public String getResourceFileName()
	{
		return "IdentifyIndirectThreatAndOpportunities.html";
	}

}
