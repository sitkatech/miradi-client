/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.main.BaseProject;

class WizardNextHandler extends WizardButtonHandler
{
	WizardNextHandler(BaseProject projectToUse)
	{
		super(projectToUse);
	}
	
	public String getDestinationStep(InterviewStepModel currentStep)
	{
		return currentStep.getNextStepName();
	}
}