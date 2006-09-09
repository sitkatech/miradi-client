/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardConstructChainsStep extends WizardStep
{

	public DiagramWizardConstructChainsStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	
	public String getResourceFileName()
	{
		return "ConstructChainsHowto.html";
	}
}
