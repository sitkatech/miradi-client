/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.io.IOException;

import org.conservationmeasures.eam.views.interview.elements.ElementData;
import org.martus.swing.UiVBox;
import org.martus.util.UnicodeReader;

class WizardStep extends UiVBox
{
	public void loadTemplate(UnicodeReader reader) throws IOException
	{
		WizardStepLoader.load(this, reader);
	}
	
	public void addComponent(ElementData elementToAdd)
	{
		add(elementToAdd.createComponent());
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