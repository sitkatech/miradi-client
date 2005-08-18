/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import javax.swing.JLabel;

import org.martus.swing.UiVBox;

class WizardStep extends UiVBox
{
	public WizardStep(String stepNameToUse)
	{
		stepName = stepNameToUse;
	}
	
	public void addText(String text)
	{
		add(new JLabel(text));
	}
	
	String getStepName()
	{
		return stepName;
	}
	
	String stepName;
}