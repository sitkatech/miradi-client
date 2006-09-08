/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class DiagramWizardPanel extends WizardPanel
{
	public DiagramWizardPanel() throws Exception
	{
		DiagramWizardOverviewStep step = new DiagramWizardOverviewStep();
		setContents(step);
		step.refresh();
	}

}
