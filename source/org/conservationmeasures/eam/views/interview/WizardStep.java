/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

class WizardStep extends JPanel
{
	public WizardStep(String stepNameToUse, String contentsToUse)
	{
		stepName = stepNameToUse;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel(contentsToUse));
	}
	
	String getStepName()
	{
		return stepName;
	}
	
	String stepName;
}