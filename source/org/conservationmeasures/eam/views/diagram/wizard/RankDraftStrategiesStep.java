/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class RankDraftStrategiesStep extends WizardStep
{
	public RankDraftStrategiesStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	String HTML_FILENAME = "RankDraftStrategies.html";
}
