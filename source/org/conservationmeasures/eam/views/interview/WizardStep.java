/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.io.IOException;

import javax.swing.JLabel;

import org.martus.swing.UiVBox;
import org.martus.util.UnicodeReader;

class WizardStep extends UiVBox
{
	public void loadTemplate(UnicodeReader reader) throws IOException
	{
		WizardStepLoader.load(this, reader);
	}
	
	public void addText(String text)
	{
		add(new JLabel(text));
	}
	
	public void setStepName(String stepNameToUse)
	{
		stepName = stepNameToUse;
	}
	
	String getStepName()
	{
		return stepName;
	}
	
	String stepName;
}